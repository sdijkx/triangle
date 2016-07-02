package nl.triangle.plant.classifier.algorithms.imagedescriptor.block;

import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.Cell;

import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * Created by steven on 26-06-16.
 */
public class ContrastNormalizeBlock<T extends Cell> {
    public DoubleStream normalize(Stream<Block<T>> blocks) {
        return blocks.flatMapToDouble(block -> DoubleStream.of(block.normalize()));
    }
}
