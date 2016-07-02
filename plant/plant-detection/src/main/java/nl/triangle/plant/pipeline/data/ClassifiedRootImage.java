package nl.triangle.plant.pipeline.data;

import java.awt.image.BufferedImage;

/**
 * Created by steven on 14-05-15.
 */
public class ClassifiedRootImage {

    private RootImage rootImage;

    public ClassifiedRootImage(RootImage img) {
        this.rootImage = img;
    }

    public int getX() { return rootImage.getX(); }
    public int getY() { return rootImage.getY(); }

    public BufferedImage getBufferedImage() {
        return rootImage.getBufferedImage();
    }

    public RootImage getRootImage() {
        return rootImage;
    }
}
