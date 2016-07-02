package nl.triangle.plant.classifier.algorithms.imagedescriptor.block;

import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.Cell;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * Created by steven on 11-06-16.
 */
public class Block<T extends Cell> {
    private final BlockStreamConfiguration<T> configuration;
    private List<T> cells;
    private final BufferedImage image;
    private final int bx, by;

    public Block(BufferedImage image, int bx, int by, BlockStreamConfiguration<T> configuration) {
        this.image = image;
        this.bx = bx;
        this.by = by;
        this.configuration = configuration;
    }

    public void compute() {
        cells = Arrays.asList(
                computeCell(bx, by),
                computeCell(bx + configuration.getCellSize(), by),
                computeCell(bx, by + configuration.getCellSize()),
                computeCell(bx + configuration.getCellSize(), by + configuration.getCellSize())
        );
    }

    private T computeCell(int bx, int by) {
        return configuration.getCellFunction().computeCell(bx, by, configuration.getCellSize(), image);
    }

    public double[] normalize() {
        //create 1 vector
        double[] vector = new double[9 * cells.size()];
        int vi = 0;
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < 9; j++) {
                vector[vi++] = cells.get(i).getVector(j);
            }
        }
        //normalize vector
        double vectorLength = Math.sqrt(DoubleStream.of(vector).map( v -> v * v).sum());

        if(vectorLength != 0.0) {
            for (int i = 0; i < vector.length; i++) {
                vector[i] /= vectorLength;
            }
        }
        return vector;
    }



}
