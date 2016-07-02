package nl.triangle.plant.classifier.algorithms;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by sdi20386 on 29-4-2015.
 */
public class Util {

    public static Matrix reshape(double[] array, int offset, int rows, int columns) {
        Matrix m = new Matrix(rows, columns);
        for(int i = 0; i < rows; i++ ) {
            for(int j = 0; j< columns; j++) {
                m.set(i, j, array[offset++]);
            }
        }
        return m;
    }

    public static Matrix ones(int i, Matrix in) {
        Matrix m = new Matrix(in.getRowDimension(), in.getColumnDimension() + i, 1.0);
        m.setMatrix(0, m.getRowDimension() - 1, i, m.getColumnDimension() -1, in );
        return m;
    }

    public static Matrix removeFirstRow(Matrix in) {
        return new Matrix(Arrays.copyOfRange(in.getArrayCopy(), 1, in.getRowDimension()));
    }

    public static double[] pack(Matrix...mList) {
        List<double[]> list = new ArrayList<>();
        int size = 0;
        for(Matrix m : mList) {
            double[] rowPacked = m.getRowPackedCopy();
            size += rowPacked.length;
            list.add(rowPacked);
        }
        double[] result = new double[size];
        int j =0;
        for(double[] r : list) {
            for(int i = 0; i < r.length; i++) {
                result[j++] = r[i];
            }
        }
        return result;
    }

    public interface MF {
        double apply(int i, int j, double v);
    }

    public static double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }


    public static double sigmoidGradient(double z) {
        return sigmoid(z)/(1.0 - sigmoid(z));
    }

    public static Matrix applyToAll(Matrix in , Function<Double, Double> func) {
        return applyToAll(in, (i,j,v) -> func.apply(v));
    }

    public static Matrix applyToAll(Matrix in , MF func) {
        Matrix result = new Matrix(in.getRowDimension(), in.getColumnDimension());
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                result.set(i,j, func.apply(i,j,in.get(i,j)));
            }
        }
        return result;
    }
    public static Matrix func(Matrix a, Matrix b, BiFunction<Double,Double, Double> func) {

        Matrix result = new Matrix(a.getRowDimension(), a.getColumnDimension());
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < a.getColumnDimension(); j++) {
                result.set(i, j, func.apply(a.get(i, j), b.get(i, j)));
            }
        }
        return result;
    }

    public static double sum(Matrix in) {
        double sum = 0;
        for (int i = 0; i < in.getRowDimension(); i++) {
            for (int j = 0; j < in.getColumnDimension(); j++) {
                sum += in.get(i,j);
            }
        }
        return sum;
    }
}
