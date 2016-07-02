package nl.triangle.plant.classifier.algorithms.coordinategroup;

/**
 * Created by steven on 16-06-16.
 */
public interface CoordinateGroupFunction<O, I, G> {
    O transformByPosition(I in, CoordinateGroup<G> position);
}
