package nl.triangle.plant.classifier.algorithms.coordinategroup;

import java.util.stream.Stream;

/**
 * Created by steven on 16-06-16.
 */
public class CoordinateGroupStreamBuilder<O,I,G> {

    public Stream<O> toStream(I in, CoordinateGroupFunction<O,I,G> transformFunction, MoveStrategy<CoordinateGroup<G>> strategy) {

        //loop through all positions
        CoordinateGroup<G> current = strategy.moveToStart();
        Stream.Builder<O> builder = Stream.builder();

        while(strategy.isLessThanWidth(current)) {
            while(strategy.isLessThanHeight(current)) {
                builder.add(transformFunction.transformByPosition(in, current));
                current = strategy.moveDown(current);
            }
            current = strategy.moveLeft(current);
            current = strategy.moveToTop(current);

        }
        return builder.build();
    }
}
