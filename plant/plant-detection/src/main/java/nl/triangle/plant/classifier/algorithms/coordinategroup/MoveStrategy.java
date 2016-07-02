package nl.triangle.plant.classifier.algorithms.coordinategroup;

/**
 * Created by steven on 16-06-16.
 */
public interface MoveStrategy<T> {

    T moveDown(T current);
    T moveLeft(T current);
    T moveToTop(T current);
    T moveToStart();
    boolean isLessThanWidth(T current);
    boolean isLessThanHeight(T current);

}
