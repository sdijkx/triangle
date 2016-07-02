package nl.triangle.plant.pipeline.dto;

/**
 * Created by steven on 14-05-16.
 */
public class PlantModel {
    private double[] box;
    private int x;
    private int y;

    public void setBox(double[] box) {
        this.box = box;
    }

    public double[] getBox() {
        return box;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return (int)(box[2]);
    }

    public int getHeight() {
        return (int)(box[3]);
    }
}
