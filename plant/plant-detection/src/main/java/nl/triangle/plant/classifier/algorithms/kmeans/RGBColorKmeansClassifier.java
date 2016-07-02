package nl.triangle.plant.classifier.algorithms.kmeans;

/**
 * Created by steven on 12-03-16.
 */
public class RGBColorKmeansClassifier implements KmeansClassifier<double[]> {

    @Override
    public double[] add(double[] item1, double[] item2) {
        return new double[] {
                item1[0] + item2[0],
                item1[1] + item2[1],
                item1[2] + item2[2]
        };
    }

    @Override
    public double[] getValueForCount(double[] item1, int divider) {
        return new double[] {
                item1[0] / ((double) divider) ,
                item1[1] / ((double) divider) ,
                item1[2] / ((double) divider)
        };
    }

    @Override
    public boolean isLess(double[] item1, double[] item2) {
        return length(item1) < length(item2);
    }

    private double length(double[] item) {
        return Math.sqrt(
                item[0] * item[0] +
                item[1] * item[1] +
                item[2] * item[2]
        );
    }

    @Override
    public double[] getWeight(double[] item1, double[] item2) {
        return new double[] {
                item1[0] - item2[0],
                item1[1] - item2[1],
                item1[2] - item2[2]
        };
    }

    @Override
    public double[] getZero() {
        return new double[] { 0.0, 0.0, 0.0 };
    }
}
