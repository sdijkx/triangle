package nl.triangle.plant.classifier.algorithms.imagedescriptor.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by steven on 27-06-16.
 */
public class ConvertToRGB {

    public BufferedImage convert(BufferedImage image) {
        BufferedImage rgb = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D gd = rgb.createGraphics();
        gd.drawImage(image, 0, 0, null);
        gd.dispose();
        return rgb;

    }
}
