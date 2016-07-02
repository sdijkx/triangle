package nl.triangle.plant.classifier.algorithms.kmeans;

/**
 * Created by steven on 12-03-16.
 */
interface KmeansData<T> {
    T get(int i);
    int size();
}
