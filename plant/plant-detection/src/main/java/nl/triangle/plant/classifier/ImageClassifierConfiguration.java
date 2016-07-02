package nl.triangle.plant.classifier;

import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptor;

/**
 * Created by steven on 12-06-16.
 */
public class ImageClassifierConfiguration {
    private final ImageDescriptor imageDescriptor;
    private final int inputSize;

    public ImageClassifierConfiguration(ImageDescriptor imageDescriptor, int inputSize) {
        this.imageDescriptor = imageDescriptor;
        this.inputSize = inputSize;
    }

    public ImageDescriptor getImageDescriptor() {
        return imageDescriptor;
    }

    public int getInputSize() {
        return inputSize;
    }
}
