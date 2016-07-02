package nl.triangle.plant.classifier.algorithms.kmeans;

import java.util.List;

/**
 * Created by steven on 12-03-16.
 */
public class KmeansClassifiedData<T> {
    private final int[] classification;
    private final List<T> means;

    KmeansClassifiedData(int[] classification, List<T> means) {
        this.classification = classification;
        this.means = means;
    }

    public int get(int i) {
        return classification[i];
    }

    public T getMean(int i) {
        return means.get(i);
    }
}
