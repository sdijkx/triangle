package nl.triangle.plant.pipeline.processors;

import nl.triangle.plant.classifier.ImageClassifier;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SlidingWindow<T> {

    private final BufferedImage image;
    private final ImageClassifier classifier;
    private final int width;
    private final int height;
    private final int step;
    private final SlideImageFunction<T> createImageFunction;

    public SlidingWindow(BufferedImage image, ImageClassifier classifier, int step, SlideImageFunction<T> createImageFunction) {
        this.classifier = classifier;
        this.image = image;
        this.width = classifier.getConfiguration().getWidth();
        this.height = classifier.getConfiguration().getHeight();
        this.step = step;
        this.createImageFunction = createImageFunction;
    }


    public SlidingWindow(BufferedImage image, ImageClassifier classifier, int width, int height, int step, SlideImageFunction<T> createImageFunction) {
        this.classifier = classifier;
        this.image = image;
        this.width = width;
        this.height = height;
        this.step = step;
        this.createImageFunction = createImageFunction;
    }

    //Run sliding windows and plant classifier
    public Stream<T> findImages() {
        return slide(image, width, height, step);
    }

    private Stream<T> slide(BufferedImage source, int w, int h, int step) {
        return IntStream.range(0, source.getWidth() - w)
                .filter(x -> isMultideOf(x, step))
                .mapToObj(x -> slideY(source, w, h, step, x))
                .flatMap(stream -> stream);
    }

    private Stream<T> slideY(BufferedImage source, int w, int h, int step, int x) {
        return IntStream.range(0, source.getHeight() - h)
                .filter(y -> isMultideOf(y, step))
                .mapToObj(y -> new Slide(source.getSubimage(x, y, w, h), y))
                .filter(this::classify)
                .map(s -> createImageFunction.slide(s.image, x, s.y));
    }

    private boolean isMultideOf(int x, int m) {
        return x % m == 0;
    }

    private boolean classify(Slide image) {
        return classifier.classifiy(Optional.of(image.image));
    }


    private static class Slide {
        final BufferedImage image;
        final int y;

        public Slide(BufferedImage image, int y) {
            this.image = image;
            this.y = y;
        }
    }

}
