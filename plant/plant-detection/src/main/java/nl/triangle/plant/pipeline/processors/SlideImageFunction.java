package nl.triangle.plant.pipeline.processors;

import java.awt.image.BufferedImage;

/**
 * Created by steven on 11-07-16.
 */
public interface SlideImageFunction<T> {
    T slide(BufferedImage image, int x, int y);
}
