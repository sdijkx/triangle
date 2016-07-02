package nl.triangle.plant.pipeline.processors;

import nl.triangle.plant.classifier.ImageClassifier;
import nl.triangle.plant.pipeline.data.RootImage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class SlidingWindow {

    private final BufferedImage image;
    private final ImageClassifier classifier;
    private final int width;
    private final int height;
    private final int step;

    public SlidingWindow(BufferedImage image, ImageClassifier classifier, int width, int height, int step) throws IOException {
        this.classifier = classifier;
        this.image = image;
        this.width = width;
        this.height = height;
        this.step = step;
    }

    //Run sliding windows and plant classifier
    public List<RootImage> process() throws IOException {
        return slide(image, width, height, step, this::classify);
    }

    private boolean classify(BufferedImage image) {
        return classifier.classifiy(Optional.of(image));
    }


    private List<RootImage> slide(BufferedImage source, int w, int h, int step, Function<BufferedImage, Boolean> classify) {
        List<RootImage> result = Collections.synchronizedList(new ArrayList<>());
        IntStream.range(0, source.getWidth() - w)
                .filter(x -> isMultideOf(x, step))
                .forEach(x -> {
                    IntStream.range(0, source.getHeight() - h).forEach(y -> {
                        if(y % step == 0) {
                            BufferedImage slidingWindow = source.getSubimage(x, y, w, h);
                            if (classify(slidingWindow)) {
                                result.add(new RootImage(slidingWindow, x, y));
                            }
                        }
                    });
                });
        return result;
    }

    private boolean isMultideOf(int x, int m) {
        return x % m == 0;
    }

}
