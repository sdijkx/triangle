package nl.triangle.plant.classifier.algorithms.imagedescriptor.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by steven on 27-05-16.
 */
public class ResizeImage {
    private int width;
    private int height;

    public ResizeImage(int imageWidth, int imageHeight) {
        this.width = imageWidth;
        this.height = imageHeight;
    }

    public BufferedImage resize(BufferedImage in) {
        BufferedImage resize = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resize.createGraphics();
        g.drawImage(in, 0, 0, width, height, null);
        g.dispose();
        return resize;
    }

}
