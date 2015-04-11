package nl.triangle.plant.model;

public class SquareModel extends FunctionalModel {

    public SquareModel() {
        super((y) -> new double[] { 1, y, y*y } );
    }
}
