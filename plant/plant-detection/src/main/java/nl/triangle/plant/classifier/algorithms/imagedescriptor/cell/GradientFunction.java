package nl.triangle.plant.classifier.algorithms.imagedescriptor.cell;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * Created by steven on 11-06-16.
 */
class GradientFunction {

    protected List<GradientVector> compute(BufferedImage image, int x, int y) {

        if (x >= image.getWidth()) {
            throw new IllegalArgumentException();
        }

        if (y >= image.getHeight()) {
            throw new IllegalArgumentException();
        }

        double[] pL;
        double[] p;
        double[] pT;

        if (x == 0) {
            pL = new double[]{0, 0, 0};
        } else {
            pL = image.getRaster().getPixel(x - 1, y, new double[3]);
        }

        if (y == 0) {
            pT = new double[]{0, 0, 0};
        } else {
            pT = image.getRaster().getPixel(x, y - 1, new double[3]);
        }

        p = image.getRaster().getPixel(x, y, new double[3]);

        List<GradientVector> vectorList = Arrays.asList(
                new GradientVector(p[0] - pL[0], p[0] - pT[0]),
                new GradientVector(p[1] - pL[1], p[1] - pT[1]),
                new GradientVector(p[2] - pL[2], p[2] - pT[2])
        );

        return vectorList;
    }
}
