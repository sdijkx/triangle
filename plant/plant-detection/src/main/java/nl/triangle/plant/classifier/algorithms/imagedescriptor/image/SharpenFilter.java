package nl.triangle.plant.classifier.algorithms.imagedescriptor.image;

public class SharpenFilter extends ConvolveOpTransform {

    private final static float[] matrix = {
            0.0f, -1.0f, 0.0f,
            -1.0f, 5.0f, -1.0f,
            0.0f, -1.0f, 0.0f
    };
    public SharpenFilter() {
        super(matrix, 3,3);
    }
}
