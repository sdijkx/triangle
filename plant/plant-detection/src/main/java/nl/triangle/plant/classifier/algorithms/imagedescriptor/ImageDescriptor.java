package nl.triangle.plant.classifier.algorithms.imagedescriptor;

import org.encog.ml.data.MLData;

import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * Created by steven on 12-06-16.
 */
public interface ImageDescriptor {
    MLData compute(Optional<BufferedImage> image);
}
