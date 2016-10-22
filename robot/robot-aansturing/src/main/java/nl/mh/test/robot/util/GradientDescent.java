package nl.mh.test.robot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.IntStream;

public class GradientDescent {
    private static Logger LOG = LoggerFactory.getLogger(GradientDescent.class);


    private final int maxIterations;
    private final double alpha;
    private final double[][] X;
    private final double[] y;
    private final double[] theta;
    private final double maxCost;


    public GradientDescent(double[][] x, double[] y, double[] theta, double maxCost, int maxIterations, double alpha) {
        this.maxIterations = maxIterations;
        this.X = x;
        this.y = y;
        this.maxCost = maxCost;
        this.theta = theta;
        this.alpha = alpha;
    }


    public double[] computeTheta() {
        int m = theta.length;
        double[] newTheta = copyArray(theta);
        double previosCost = cost(X, y, theta);
        double workAlpha = alpha;
        for (int h = 0; h < maxIterations; h++) {
            double[] tmpTheta = IntStream.range(0, theta.length)//.asDoubleStream()
                    .mapToDouble(
                            j -> newTheta[j] - IntStream.range(0, m).parallel().mapToDouble(
                                    i -> (
                                            IntStream.range(0, X[0].length).mapToDouble(ix -> ((X[i][ix] * newTheta[ix]))).sum()
                                                    - y[i]) * X[i][j]).sum()
                                    * alpha / m).toArray();
            if (h % 10000 == 0) {
                double cost = cost(X, y, tmpTheta);
                if (cost < maxCost) return tmpTheta;
                LOG.info("-h     --> {}", h);
                LOG.info( "--------------costs --> {}",cost(X, y, tmpTheta));
                LOG.info( "--------------tmpTheta --> {}",tmpTheta );
            }
            IntStream.range(0, theta.length).forEach(i -> newTheta[i] = tmpTheta[i]);
        }

        return newTheta;
    }

    private double[] copyArray(double[] array) {
        double[] newArray = new double[array.length];
        IntStream.range(0, theta.length).forEach(i -> newArray[i] = array[i]);
        return newArray;

    }


    public double cost(double[][] X, double[] y, double[] newTheta) {
        return IntStream.range(0, y.length).parallel().mapToDouble(i ->
                Math.pow(
                        (IntStream.range(0, X[0].length).mapToDouble(ix -> ((X[i][ix] * newTheta[ix]))).sum()
                                - y[i]), 2)).sum() / 2 * y.length;
    }
}
