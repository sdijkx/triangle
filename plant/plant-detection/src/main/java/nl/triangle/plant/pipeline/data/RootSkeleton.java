package nl.triangle.plant.pipeline.data;

import nl.triangle.plant.classifier.PlantGrowthDirection;
import nl.triangle.plant.classifier.RootDetector;
import nl.triangle.plant.classifier.algorithms.trendmodel.FunctionalModel;
import nl.triangle.plant.classifier.algorithms.trendmodel.TrendLine;

import java.awt.image.BufferedImage;

/**
 * Created by steven on 14-05-15.
 */
public class RootSkeleton {

    private final ClassifiedRootImage classifiedRootImage;
    private final TrendLine trendLine;
    private final int x, y;


    public RootSkeleton(ClassifiedRootImage img) {
        try {
            classifiedRootImage = img;
            RootDetector rootDetector = new RootDetector();
            BufferedImage copy = rootDetector.findPixels(img.getBufferedImage());
            FunctionalModel functionalModel = new FunctionalModel((y) -> new double[]{1, y, y*y, Math.sqrt(y) });
            PlantGrowthDirection plantGrowthDirection = new PlantGrowthDirection(functionalModel);
            trendLine = plantGrowthDirection.calculate(copy);
            x = img.getX();
            y = img.getY();
        } catch (Exception e) {
            throw new IllegalStateException("Error calculating growth direction",e);
        }
    }

    public TrendLine getTrendLine() {
        return trendLine;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ClassifiedRootImage getClassifiedRootImage() {
        return classifiedRootImage;
    }
}
