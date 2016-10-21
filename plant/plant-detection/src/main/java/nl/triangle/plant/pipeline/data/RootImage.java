package nl.triangle.plant.pipeline.data;

import java.awt.image.BufferedImage;

/**
 * Created by steven on 14-05-15.
 */
public class RootImage {
    private final int x;
    private final int y;
    private final BufferedImage rootImage;
    private final PlantImage plantImage;

    public RootImage(BufferedImage rootImage, int x, int y, PlantImage plantImage) {
        this.x = x;
        this.y = y;
        this.rootImage = rootImage;
        this.plantImage = plantImage;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getBufferedImage() {
        return rootImage;
    }
}
