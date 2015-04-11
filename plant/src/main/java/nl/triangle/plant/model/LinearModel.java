package nl.triangle.plant.model;

public class LinearModel extends FunctionalModel {

    public LinearModel() {
        super((y) -> new double[] { 1, y } );
    }
}
