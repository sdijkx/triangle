package nl.triangle.plant.classifier.algorithms.imagedescriptor.block;

import nl.triangle.plant.classifier.algorithms.coordinategroup.CoordinateGroup;
import nl.triangle.plant.classifier.algorithms.coordinategroup.CoordinateGroupStreamBuilder;
import nl.triangle.plant.classifier.algorithms.coordinategroup.IntCoordinateGroup;
import nl.triangle.plant.classifier.algorithms.coordinategroup.MoveStrategy;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.Cell;

import java.awt.image.BufferedImage;
import java.util.stream.Stream;

/**
 * Created by steven on 16-06-16.
 */
public class ImageToBlockStream<T extends Cell> {

    private final BlockStreamConfiguration blockStreamConfiguration;

    public ImageToBlockStream(BlockStreamConfiguration blockStreamConfiguration) {
        this.blockStreamConfiguration = blockStreamConfiguration;
    }

    public Stream<Block<T>> toStream(BufferedImage gradientImage) {
        return toStream(gradientImage, new MoveStrategy<CoordinateGroup<Integer>>() {

            @Override
            public CoordinateGroup<Integer> moveDown(CoordinateGroup<Integer> current) {
                return new IntCoordinateGroup(current.x(), current.y() + blockStreamConfiguration.getCellSize());
            }

            @Override
            public CoordinateGroup<Integer> moveLeft(CoordinateGroup<Integer> current) {
                return new IntCoordinateGroup(current.x() + blockStreamConfiguration.getCellSize(), current.y());
            }

            @Override
            public CoordinateGroup<Integer> moveToTop(CoordinateGroup<Integer> current) {
                return new IntCoordinateGroup(current.x(), 0);
            }

            @Override
            public CoordinateGroup<Integer> moveToStart() {
                return new IntCoordinateGroup(0,0);
            }

            @Override
            public boolean isLessThanWidth(CoordinateGroup<Integer> current) {
                return current.x() < blockStreamConfiguration.getBlockWidth() * blockStreamConfiguration.getCellSize();
            }

            @Override
            public boolean isLessThanHeight(CoordinateGroup<Integer> current) {
                return current.y() < blockStreamConfiguration.getBlockHeight() * blockStreamConfiguration.getCellSize();
            }
        });
    }

    public Stream<Block<T>> toStream(BufferedImage gradientImage, MoveStrategy<CoordinateGroup<Integer>> strategy) {

        CoordinateGroupStreamBuilder<Block<T>, BufferedImage, Integer> streamBuilder = new CoordinateGroupStreamBuilder<>();
        return streamBuilder.toStream(
                gradientImage,
                this::toBlock,
                strategy);
    }

    private Block<T> toBlock(BufferedImage image, CoordinateGroup<Integer> position) {
        Block<T> block = new Block(image, position.x(), position.y(), blockStreamConfiguration);
        block.compute();
        return block;
    }
}
