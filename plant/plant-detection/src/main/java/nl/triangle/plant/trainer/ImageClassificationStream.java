package nl.triangle.plant.trainer;

import nl.triangle.plant.classifier.ImageClassifierSVM;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by steven on 27-06-16.
 */
public class ImageClassificationStream {

    public static Stream<ClassificationResult> applyClassifier(ImageClassifierSVM plantClassifier, Path samplePath) throws IOException {
        return Files.list(samplePath).filter(p -> p.toFile().isFile()).filter(p -> p.toFile().getName().endsWith(".png")).map(p -> {
            try {
                Optional<BufferedImage> image = Optional.of(ImageIO.read(p.toFile()));
                boolean classified = plantClassifier.classifiy(image);
                double val = plantClassifier.compute(image);
                return new ClassificationResult(classified, val);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

}
