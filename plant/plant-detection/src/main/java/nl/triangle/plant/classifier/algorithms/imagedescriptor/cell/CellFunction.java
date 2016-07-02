package nl.triangle.plant.classifier.algorithms.imagedescriptor.cell;

import java.awt.image.BufferedImage;

/**
 * Created by steven on 16-06-16.
 */
public interface CellFunction<T extends Cell> {
    T computeCell(int dx, int dy, int cellSize, BufferedImage image);
}
