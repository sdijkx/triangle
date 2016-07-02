package nl.triangle.plant.classifier.algorithms.trendmodel;

public class SquareModel extends FunctionalModel {

    public SquareModel() {
        super((y) -> new double[] { 1, y, y*y } );
    }
}
