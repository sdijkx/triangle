package nl.triangle.plant.classifier;

import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * Created by steven on 27-04-16.
 */
public interface ImageClassifier {
    boolean classifiy(Optional<BufferedImage> image);
}
