package nl.triangle.plant.classifier.algorithms.kmeans;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * Created by steven on 12-03-16.
 */
public class ImageKmeansData implements KmeansData<double[]> {

    private final BufferedImage image;
    private final int width;
    private final int height;
    private final int size;
    private final Raster raster;

    public ImageKmeansData(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.raster = image.getData();
        this.size = width * height;
    }

    @Override
    public double[] get(int i) {
        int x = i % width;
        int y = i / width;
        return raster.getPixel(x, y, new double[3]);
    }

    @Override
    public int size() {
        return size;
    }

    public int[] getCoordinatesByIndex(int index) {
        return new int[] { index % width, index / width };

    }
}
