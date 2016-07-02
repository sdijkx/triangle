package nl.triangle.plant.trainer;

/**
 * Created by steven on 27-06-16.
 */
class ClassificationResult {
    private final boolean classified;
    private final double value;

    public ClassificationResult(boolean classified, double value) {
        this.classified = classified;
        this.value = value;
    }

    public boolean isClassified() {
        return classified;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ClassificationResult{" +
                "classified=" + classified +
                ", value=" + value +
                '}';
    }
}
