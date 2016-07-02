package nl.triangle.plant.classifier.algorithms.kmeans;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class WeightedImage {

    private final BufferedImage image;
    private final int width;
    private final int height;
    private final int size;
    private final Raster raster;

    WeightedImage(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.raster = image.getData();
        this.size = width * height;
    }

    public double getWeightOf(int x, int y) {
        return 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return size;
    }
}
