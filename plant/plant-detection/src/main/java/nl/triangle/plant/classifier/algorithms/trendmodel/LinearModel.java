package nl.triangle.plant.classifier.algorithms.trendmodel;

public class LinearModel extends FunctionalModel {

    public LinearModel() {
        super((y) -> new double[] { 1, y } );
    }
}
