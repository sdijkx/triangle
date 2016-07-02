package nl.triangle.plant.classifier.algorithms.coordinatesets;

/**
 * Created by steven on 10-04-16.
 */
interface Moderator<I,O> {
    O moderate(I in);
}
