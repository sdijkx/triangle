package nl.triangle.plant.classifier.algorithms.kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by steven on 12-03-16.
 */
public class KMeansAlgorithm<T> {

    private final KmeansClassifier<T> kmeansClassifier;
    private final int numberOfClasses;
    private final int K;

    public KMeansAlgorithm(KmeansClassifier<T> kmeansClassifier, int numberOfClasses, int k) {
        this.kmeansClassifier = kmeansClassifier;
        this.numberOfClasses = numberOfClasses;
        K = k;
    }

    public KmeansClassifiedData<T> classifyInput(KmeansData<T> kmeansData) {
        int[] classifications = new int[kmeansData.size()];
        List<T> means = new ArrayList<>(numberOfClasses);
        initializeMeans(means, kmeansData);
        for(int k = 0 ; k < K; k++) {
            classify(classifications, kmeansData, means);
            means = centroid(classifications, kmeansData);
        }
        return new KmeansClassifiedData(classifications, means);
    }

    private void initializeMeans(List<T> means, KmeansData<T> kmeansData) {
        Random rn = new Random();
        for (int i = 0; i < numberOfClasses; i++) {
            means.add(kmeansData.get(rn.nextInt(kmeansData.size())));
        }
    }

    private void classify(int[] classifications, KmeansData<T> kmeansData, List<T> means) {
        for(int i = 0; i < kmeansData.size(); i++ ) {
            int set = findMinimum(kmeansData.get(i), means);
            classifications[i] = set;
        }
    }

    private List<T> centroid(int[] classifications, KmeansData<T> kmeansData) {
        List<T> sum = initializeToZero(numberOfClasses, kmeansClassifier.getZero());
        int[] count = new int[numberOfClasses];
        for(int i = 0; i < kmeansData.size(); i++ ) {
            T result = kmeansClassifier.add(sum.get(classifications[i]), kmeansData.get(i));
            sum.set(classifications[i],result);
            count[classifications[i]]++;
        }

        List<T> resultMeans = initializeToZero(numberOfClasses, kmeansClassifier.getZero());
        for (int i = 0; i < sum.size(); i++) {
            if(count[i] > 0) {
                T result = kmeansClassifier.getValueForCount(sum.get(i), count[i]);
                resultMeans.set(i, result);
            }
        }
        return resultMeans;
    }

    private List<T> initializeToZero(int n, T zero) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(zero);
        }
        return list;
    }

    private int findMinimum(T x, List<T> means) {
        T minimumWeight = null;
        int minimum = -1;
        for (int i = 0; i < numberOfClasses; i++) {
            T weight = kmeansClassifier.getWeight(x, means.get(i));
            if(minimumWeight == null || kmeansClassifier.isLess(weight, minimumWeight)) {
                minimumWeight = weight;
                minimum = i;
            }
        }
        return minimum;
    }

}
