package nl.triangle.plant.classifier.algorithms.imagedescriptor.cell;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steven on 16-06-16.
 */
public class ComputeHistogram implements CellFunction<Histogram> {

    @Override
    public Histogram computeCell(int dx, int dy, int cellSize, BufferedImage image) {
        List<GradientVector> cellGradientVectors = new ArrayList<>();
        GradientFunction gradientFunction = new GradientFunction();
        for (int x = 0; x < cellSize; x++) {
            for (int y = 0; y < cellSize; y++) {
                List<GradientVector> gradientVectors = gradientFunction.compute(image, x + dx, y + dy);
                cellGradientVectors.addAll(gradientVectors);
            }
        }
        Histogram histogram = new Histogram();
        histogram.compute(cellGradientVectors);
        return histogram;
    }

}
