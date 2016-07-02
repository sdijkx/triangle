package nl.triangle.plant.classifier.algorithms.kmeans;

/**
 * Created by steven on 12-03-16.
 */
public interface KmeansClassifier<T> {
    T add(T item1, T item2);
    T getValueForCount(T item1, int divider);
    boolean isLess(T item1, T item2);
    T getWeight(T item1, T item2);
    T getZero();
}
