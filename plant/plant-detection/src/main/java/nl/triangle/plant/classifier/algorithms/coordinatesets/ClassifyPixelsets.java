package nl.triangle.plant.classifier.algorithms.coordinatesets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by steven on 10-04-16.
 */
public class ClassifyPixelsets {

    private Function<Color,Integer> moderator = in ->in.getRGB();

    private final BufferedImage image;
    private Map<Integer,CoordinateSet> classes = new HashMap<>();

    public ClassifyPixelsets(BufferedImage image) {
        this.image = image;
    }

    public void createClasses() {
        Raster raster = image.getData();
        for (int x = 0; x < raster.getWidth(); x++) {
            for (int y = 0; y < raster.getHeight(); y++) {
                double[] pixel = new double[3];
                raster.getPixel(x,y,pixel);
                Color color = new Color((int)pixel[0], (int)pixel[1], (int)pixel[2]);
                int classification = moderator.apply(color);
                CoordinateSet coordinateset = Optional.ofNullable(classes.get(classification)).orElseGet(CoordinateSet::new);
                coordinateset.add(new Coordinate(x,y));
                classes.put(classification, coordinateset);
            }
        }
    }

    public Map<Integer, CoordinateSet> getClasses() {
        return Collections.unmodifiableMap(classes);
    }
}
