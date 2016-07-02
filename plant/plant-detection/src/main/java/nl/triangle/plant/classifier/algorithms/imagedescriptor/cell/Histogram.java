package nl.triangle.plant.classifier.algorithms.imagedescriptor.cell;

import java.util.List;

/**
 * Created by steven on 11-06-16.
 */
class Histogram implements Cell {
    private double[] histogram = new double[9];

    public void compute(List<GradientVector> gradientVectors) {
        for (GradientVector gradientVector : gradientVectors) {
            double bin = Math.floor(Math.abs(gradientVector.getAngle()) / 9.0) % 9;
            double binCenter = bin * 20;
            double split = (binCenter - gradientVector.getAngle()) / 20.0;
            int binIndex = (int) bin;
            histogram[binIndex] += split * gradientVector.getMagnitude();
            histogram[(binIndex + 1) % 9] += (1.0 - split) * gradientVector.getMagnitude();
        }
    }


    @Override
    public double[] getVector() {
        return histogram;
    }

    @Override
    public double getVector(int i) {
        return histogram[i];
    }
}
