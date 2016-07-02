package nl.triangle.plant.classifier.algorithms.trendmodel;

public abstract class TrendModel {

    private double[][] theta;
    public abstract double[] getFeatures(double y);

    public void setTheta(double[][] theta) {
        this.theta = theta;
    }

    public int getX(double y) {

        double[] features = getFeatures(y);
        double x = 0;
        for(int i = 0; i < theta.length; i++) {
            x += features[i] * theta[i][0];
        }
        return (int) Math.floor(x);
    }
}
