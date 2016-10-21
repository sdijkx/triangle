package nl.triangle.plant.classifier;

import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptor;

/**
 * Created by steven on 12-06-16.
 */
public class ImageClassifierConfiguration {
    private final ImageDescriptor imageDescriptor;
    private final int inputSize;
    private final int width;
    private final int height;

    public ImageClassifierConfiguration(ImageDescriptor imageDescriptor, int inputSize, int width, int height) {
        this.imageDescriptor = imageDescriptor;
        this.inputSize = inputSize;
        this.width = width;
        this.height = height;
    }

    public ImageDescriptor getImageDescriptor() {
        return imageDescriptor;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
