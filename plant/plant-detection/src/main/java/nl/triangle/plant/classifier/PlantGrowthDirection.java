package nl.triangle.plant.classifier;

import nl.triangle.plant.classifier.algorithms.coordinatesets.Coordinate;
import nl.triangle.plant.classifier.algorithms.trendmodel.FunctionalModel;
import nl.triangle.plant.classifier.algorithms.trendmodel.TrendLine;
import nl.triangle.plant.classifier.algorithms.trendmodel.TrendModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sdi20386 on 11-4-2015.
 */
public class PlantGrowthDirection {

    private final TrendModel trendModel;

    public PlantGrowthDirection(TrendModel trendModel) {
        this.trendModel = trendModel;
    }

    public TrendLine calculate(BufferedImage image) throws IOException {

        Set<Coordinate> coordinateSet = getCoordinates(image);
        if(coordinateSet.size() > 1) {
            TrendLine trendLine = new TrendLine(coordinateSet);
            trendLine.estimate(trendModel);
            return trendLine;
        }
        throw new IllegalStateException("Empty coordinate set");
    }

    public void drawTrendLine(BufferedImage image, TrendLine trendLine) {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.cyan);
        g2d.setStroke(new BasicStroke());
        double[] box = trendLine.getBox();
        g2d.drawRect((int) box[0], (int) box[1], (int) (box[2] - box[0]), (int) (box[3] - box[1]));
        g2d.drawLine(trendModel.getX(0) + (int) box[0], (int) box[1], trendModel.getX(box[3] - box[1]) + (int) box[0], (int) box[3]);
        g2d.dispose();
    }

    private Set<Coordinate> getCoordinates(BufferedImage image) {
        Set<Coordinate> coordinateSet = new HashSet<Coordinate>();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                if ((color & 0xffffffff) == 0xffffffff) {
                    coordinateSet.add(new Coordinate(i, j));
                }
            }
        }
        return coordinateSet;
    }


}
