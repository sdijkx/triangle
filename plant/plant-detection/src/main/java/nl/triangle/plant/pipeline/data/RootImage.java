package nl.triangle.plant.pipeline.data;

import java.awt.image.BufferedImage;

/**
 * Created by steven on 14-05-15.
 */
public class RootImage {
    private int x;
    private int y;
    private BufferedImage rootImage;

    public RootImage(BufferedImage rootImage, int x, int y) {
        this.x = x;
        this.y = y;
        this.rootImage = rootImage;
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
