package nl.triangle.plant;

import nl.triangle.plant.model.FunctionalModel;
import nl.triangle.plant.model.SquareModel;
import nl.triangle.plant.model.TrendLine;
import nl.triangle.plant.model.TrendModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sdi20386 on 11-4-2015.
 */
public class ImageScanner {

    public static void main(String...args)  {

        try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("root-simple-2.png")) {

            BufferedImage image = javax.imageio.ImageIO.read(in);

            Set<Coordinate> coordinateSet = new HashSet<Coordinate>();

            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    int color = image.getRGB(i, j);
                    if ((color & 0xffffffff) == 0xffffffff) {
                        coordinateSet.add(new Coordinate(i, j));
                    }
                }
            }

            TrendModel trendModel = new FunctionalModel( (y) -> new double[] { 1 , y } );
            TrendLine trendLine = new TrendLine(coordinateSet);
            trendLine.estimate(trendModel);

            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.cyan);
            g2d.setStroke(new BasicStroke());
            double[] box = trendLine.getBox();
            g2d.drawRect((int)box[0], (int)box[1],(int)(box[2] - box[0]), (int)(box[3] - box[1]));
            g2d.drawLine(trendModel.getX(0) + (int)box[0] , (int)box[1],  trendModel.getX(box[3] - box[1]) + (int) box[0], (int)box[3]);

            File outputFile = new File("output.png");
            System.out.println(outputFile.getAbsolutePath());

            ImageIO.write(image, "png", outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
