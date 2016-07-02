package nl.triangle.plant.classifier.algorithms.imagedescriptor.image;

public class EdgeDetectionFilter extends ConvolveOpTransform {

    private final static float[] matrix = {
            -1.0f, -1.0f, -1.0f,
            -1.0f, 8.0f, -1.0f,
            -1.0f, -1.0f, -1.0f
    };
    public EdgeDetectionFilter() {
        super(matrix, 3,3);
    }
}
