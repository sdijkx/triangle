package nl.triangle.plant.classifier.algorithms.kmeans;

/**
 * Created by steven on 12-03-16.
 */
class WeightedImageKmeansData implements KmeansData<Double> {

    private final WeightedImage weightedImage;

    WeightedImageKmeansData(WeightedImage weightedImage) {
        this.weightedImage = weightedImage;
    }

    @Override
    public Double get(int i) {
        int x = i % weightedImage.getWidth();
        int y = i / weightedImage.getWidth();
        return weightedImage.getWeightOf(x, y);
    }

    @Override
    public int size() {
        return weightedImage.getSize();
    }
}
