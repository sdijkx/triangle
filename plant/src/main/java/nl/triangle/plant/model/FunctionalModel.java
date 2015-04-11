package nl.triangle.plant.model;

import java.util.function.Function;

public class FunctionalModel extends TrendModel {

    private final Function<Double, double[]> func;

    public FunctionalModel(Function<Double, double[]> func) {
        this.func = func;
    }

    @Override
    public double[] getFeatures(double y) {
        return func.apply(y);
    }
}
