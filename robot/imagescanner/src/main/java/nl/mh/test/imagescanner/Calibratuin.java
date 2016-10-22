package nl.mh.test.imagescanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Marc on 31-8-2016.
 */
public class Calibratuin {
    public static int M = 3200;
    private static Logger LOG = Logger.getLogger(Calibratuin.class.getName());


    public double getY(int xRequested) {
        List trainingSet = new ArrayList<>();

        double[][] x = new double[M][3];
        double[] y = new double[M];
        double[] theta = new double[3];

        theta[0]= 1;
        theta[1]= 1;
        theta[2]= 1;

//        IntStream.range(0, M)
//                     .forEach(i -> addResult(x, y, i, ((2 * Math.pow(i, 2)) + (0.1 * i) - 50000)));

        IntStream.range(0, M)
                .forEach(i -> addResult(x, y, i, ((1 * Math.pow(i-1600, 2)) + (1 * i-1600) )));

        GradientDescent gd = new GradientDescent(x, y, theta, 0.00001, 10000000, 0.1);
        double[] newTheta = gd.computeTheta();

        double[] xAttributes = getX(xRequested);
        return IntStream.range(0, 3).mapToDouble(i -> (xAttributes[i] * newTheta[i])).sum();

    }


    public static <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }

    private void addResult(double[][] X, double[] y, int x, double yValue) {
        X[x][0] = 1.0;
        X[x][1] = x-1600;
        X[x][2] = Math.pow(x-1600, 2);

        X[x] = getX(x);
        y[x] = yValue;
    }

    private double[] getX(int x) {
        double[] line = new double[3];
        line[0] = 1.0;
        line[1] = x;
        line[2] = Math.pow(x, 2);
        return line;
    }



}






