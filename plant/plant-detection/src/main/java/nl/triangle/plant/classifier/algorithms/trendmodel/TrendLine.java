package nl.triangle.plant.classifier.algorithms.trendmodel;

import Jama.Matrix;
import nl.triangle.plant.classifier.algorithms.coordinatesets.Coordinate;

import java.util.Set;

public class TrendLine {

    private final Set<Coordinate> set;

    public TrendLine(Set<Coordinate> set) {
        this.set = set;
    }

    private double[] box;

    public void estimate(TrendModel model) {

        double[][] feature = new double[set.size()][];
        double[][] outcome = new double[set.size()][1];

        box = boundingBox();

        int i = 0;
        for(Coordinate coordinate : set) {
            feature[i] = model.getFeatures(coordinate.y - box[1]);
            outcome[i][0] = coordinate.x - box[0];
            i++;
        }

        Matrix Y = new Matrix(outcome); // 1, m
        Matrix M = new Matrix(feature);// m, n
        Matrix MT = M.transpose();// n, m

        //n, m * m,n (n,n) * n,m  * (m,1) = (n,1)
        Matrix theta = MT.times(M).inverse().times(MT).times(Y);
        model.setTheta(theta.getArrayCopy());

    }

    public double[] getBox() {
        return box;
    }

    private double[] boundingBox() {
        double[] box = { Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE };
        for(Coordinate coordinate : set) {
            if(box[0] > coordinate.x) {
                box[0] = coordinate.x;
            }
            if(box[1] > coordinate.y) {
                box[1] = coordinate.y;
            }
            if(box[2] < coordinate.x) {
                box[2] = coordinate.x ;
            }
            if(box[3] < coordinate.y ) {
                box[3] = coordinate.y;
            }
        }
        return box;
    }

}
