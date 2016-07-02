package nl.triangle.plant.classifier.algorithms.imagedescriptor.image;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Created by steven on 26-06-16.
 */
public class ConvolveOpTransform {

    private final float[] matrix;
    private final int width;
    private final int height;

    public ConvolveOpTransform(float[] matrix, int width, int height) {
        this.matrix = matrix;
        this.width = width;
        this.height = height;
    }

    public BufferedImage transform(BufferedImage in) {
        BufferedImageOp gradientOp = new ConvolveOp(new Kernel(3, 2, matrix ), ConvolveOp.EDGE_NO_OP, null);
        BufferedImage filterImage = gradientOp.filter(in, null);
        return filterImage;

    }
}
