package nl.triangle.plant.classifier;

import nl.triangle.plant.classifier.algorithms.coordinatesets.ClassifyPixelsets;
import nl.triangle.plant.classifier.algorithms.coordinatesets.CoordinateSet;
import nl.triangle.plant.classifier.algorithms.kmeans.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RootDetector {

    public BufferedImage findPixels(BufferedImage image) {
        KmeansClassifier<double[]> kmeansClassifier = new RGBColorKmeansClassifier();
        ImageKmeansData imageData = new ImageKmeansData(image);
        KMeansAlgorithm<double[]> kMeans = new KMeansAlgorithm<>(kmeansClassifier, 4, 50);
        KmeansClassifiedData<double[]> kmeansClassifiedData = kMeans.classifyInput(imageData);

        //TODO remove this step
        BufferedImage reducedImage = writeToCopy(image, imageData, kmeansClassifiedData);

        ClassifyPixelsets classifyPixelsets = new ClassifyPixelsets(reducedImage);
        classifyPixelsets.createClasses();
        CoordinateSet selection = classifyPixelsets.getClasses().values().stream().sorted((a,b) -> {
            return Double.compare(a.getAspectRatio(), b.getAspectRatio());
        }).findFirst().orElseThrow(() -> new IllegalStateException());
        return writeToCopy(image, selection);

    }

    private BufferedImage writeToCopy(BufferedImage image, ImageKmeansData imageData, KmeansClassifiedData<double[]> kmeansClassifiedData) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < imageData.size(); i++) {
            int[] coord = imageData.getCoordinatesByIndex(i);
            double[] mean = kmeansClassifiedData.getMean(kmeansClassifiedData.get(i));
            Color color = new Color((int)mean[0], (int)mean[1], (int)mean[2]);
            out.setRGB(coord[0], coord[1], color.getRGB());
        }
        return out;
    }

    private BufferedImage writeToCopy(BufferedImage image, CoordinateSet coordinateSet) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        setBackground(out, Color.BLACK);
        coordinateSet.getCoordinates().stream().forEach(coordinate -> {
            Color color = Color.WHITE;
            out.setRGB((int) coordinate.getX(), (int) coordinate.getY(), color.getRGB());
        });
        return out;
    }

    private void setBackground(BufferedImage image, Color color) {
        Graphics2D    graphics = image.createGraphics();
        graphics.setPaint ( color );
        graphics.fillRect ( 0, 0, image.getWidth(), image.getHeight() );
    }


}

