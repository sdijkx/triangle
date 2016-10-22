package nl.mh.test.robot.calibration;

import nl.mh.test.robot.converter.CoordinateConverter;
import nl.mh.test.robot.domain.*;
import nl.mh.test.robot.domain.Robot;
import nl.mh.test.robot.imagerecognition.FindImage;
import nl.mh.test.robot.imagerecognition.ImageNotFoundException;
import nl.mh.test.robot.imagerecognition.converters.Converter;
import nl.mh.test.robot.imagerecognition.converters.GrayScaleConverter;
import nl.mh.test.robot.imagerecognition.loopstrategy.FromCenterLoopStrategy;
import nl.mh.test.robot.imagerecognition.loopstrategy.LoopStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Marc on 21-6-2016.
 */
public class CenterProcessor {
    private static Logger LOG = LoggerFactory.getLogger(CenterProcessor.class);
    private final Robot robot;
    private final Configuration config;
    private final CoordinateConverter coordinateConverter;

    public CenterProcessor(Robot robot, Configuration config) {
        this.robot = robot;
        this.config = config;
        coordinateConverter = new CoordinateConverter(config);
    }

    public Coordinates findCenter() {
        PhotoCoordinates spotCoordinates = findSpot();
        LOG.info("Spot found in {}, picture made on: {}", spotCoordinates, robot.getFlatPosition());
        return coordinateConverter.convert(robot.getFlatPosition(), spotCoordinates);
    }

    public PhotoCoordinates findSpot() {
        Photo photo = robot.createPhoto();
        try {
            BufferedImage baseImage = getBufferedImage(photo);
            LOG.info("process image {} x {}", baseImage.getWidth(), baseImage.getHeight());
            PhotoCoordinates coordinates = findImage(baseImage, "network/calibration-stip-preface.eg", 0.90, 40, 15);

            BufferedImage subImage = getSubImage(baseImage, coordinates.getPixelX() - 40, coordinates.getPixelY() - 40, 100);
            PhotoCoordinates coordinatesSub = findBestImage(subImage, "network/calibration-stip.eg", 0.90, 28, 1);

            return coordinates.add(coordinatesSub);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private BufferedImage getBufferedImage(Photo photo) {
        BufferedImage im = null;
        for (int i = 0; i < 100; i++) {
            try (InputStream in = Files.newInputStream(Paths.get(photo.getImageFile().getAbsolutePath()))) {
                im = ImageIO.read(new File(photo.getImageFile().getAbsolutePath()));
                if (im != null) {
                    break;
                }
                Thread.sleep(100);
            } catch (Exception e) {
                //
            }
        }
        return im;
    }

    private File getNetworkFile(String filename) {
        URL prefaceUri = Thread.currentThread().getContextClassLoader().getResource(filename);
        return new File(prefaceUri.getPath());
    }

    private PhotoCoordinates findImage(BufferedImage baseImage, String netwerkFile, double threshold, int bloksize, int step) throws IOException, ImageNotFoundException {
        LoopStrategy loopStrategy = new FromCenterLoopStrategy(baseImage, bloksize, step);
        Converter converter = new GrayScaleConverter();
        FindImage findImage = new FindImage(loopStrategy, netwerkFile, converter, threshold);
        PhotoCoordinates coordinates = findImage.findImage(Coordinates.HOME);
        return coordinates;
    }

    private PhotoCoordinates findBestImage(BufferedImage baseImage, String netwerkFile, double threshold, int bloksize, int step) throws IOException, ImageNotFoundException {
        LoopStrategy loopStrategy = new FromCenterLoopStrategy(baseImage, bloksize, step);
        Converter converter = new GrayScaleConverter();
        FindImage findImage = new FindImage(loopStrategy, netwerkFile, converter, threshold);
        PhotoCoordinates coordinates = findImage.findBest(Coordinates.HOME);
        return coordinates;
    }

    private BufferedImage getSubImage(BufferedImage parent, int x, int y, int size) {
        try {
            return parent.getSubimage(x, y, size, size);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
