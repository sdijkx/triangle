package nl.mh.test.robot.imagerecognition;

import nl.mh.test.robot.domain.Coordinates;
import nl.mh.test.robot.domain.PhotoCoordinates;
import nl.mh.test.robot.imagerecognition.converters.Converter;
import nl.mh.test.robot.imagerecognition.converters.GrayScaleConverter;
import nl.mh.test.robot.imagerecognition.loopstrategy.FromCenterLoopStrategy;
import nl.mh.test.robot.imagerecognition.loopstrategy.LoopStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Marc on 28-6-2016.
 */
public class FindImageTest {
    private static Logger LOG = LoggerFactory.getLogger(FindImageTest.class);

    @BeforeClass
    public void setUp() throws IOException {
        Files.createDirectories(Paths.get("target/test-result/"));
    }

    @Test
    public void testAllCalibration() throws Exception {
        Files.walk(Paths.get(Thread.currentThread().getContextClassLoader().getResource("calibration-stip").toURI()))
                .filter(Files::isRegularFile)
                .forEach(this::testImageFile);
    }

    //@Test
    public void testCalibrationFile() throws Exception {
        testImageFile(Paths.get(Thread.currentThread().getContextClassLoader().getResource("calibration-stip/image_2823514386960342658.jpg").toURI()));

        File file = File.createTempFile("image_", ".jpg");
        LOG.info("formats", ImageIO.getReaderFormatNames());
    }

    private void testImageFile(Path file) {
        LOG.info("Test file --> " + file.getFileName().toString());
        long start = System.currentTimeMillis();
        PhotoCoordinates coordinates = null;
        try (InputStream in = Files.newInputStream(file)) {

            BufferedImage baseImage = ImageIO.read(in);
           ImageIO.write(baseImage, "jpg", new File("target/test-result/result_" + System.currentTimeMillis() + "_ORG.jpg"));
            coordinates = findImage(start, baseImage, "network/calibration-stip-preface.eg", 0.90, 40, 15);

            BufferedImage subImage = getSubImage(baseImage, coordinates.getPixelX() - 5, coordinates.getPixelY() - 5, 60);
                    ImageIO.write(subImage, "jpg", new File("target/test-result/result_" + System.currentTimeMillis() + "_ORG.jpg"));
            coordinates = findImage(start, subImage, "network/calibration-stip.eg", 0.90, 28, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //@Test
    public void testImagePipet() throws Exception {
        long start = System.currentTimeMillis();
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("11-20-2015_15.02.26_testset.jpg")) {
            BufferedImage baseImage = ImageIO.read(in);
            findImage(start, baseImage, "network/pipet.eg", 0.97, 43, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PhotoCoordinates findImage(long start, BufferedImage baseImage, String netwerkFile, double threshold, int bloksize, int step) throws IOException, ImageNotFoundException {

        LoopStrategy loopStrategy = new FromCenterLoopStrategy(baseImage, bloksize, step);
        Converter converter = new GrayScaleConverter();
        FindImage findImage = new FindImage(loopStrategy, netwerkFile, converter, threshold);
        PhotoCoordinates coordinates = findImage.findBest(Coordinates.HOME);


        BufferedImage saveImage = deepCopy(baseImage);
        Graphics g = saveImage.getGraphics();
        g.setColor(Color.RED);
        g.drawRect(coordinates.getPixelX(), coordinates.getPixelY(), bloksize, bloksize);
        g.dispose();
        ImageIO.write(saveImage, "jpg", new File("target/test-result/result_" + System.currentTimeMillis() + ".jpg"));

        LOG.info("time: " + ((System.currentTimeMillis() - start)));

        return coordinates;
    }


    private BufferedImage getSubImage(BufferedImage parent, int x, int y, int size) {
        try {
            return parent.getSubimage(x, y, size, size);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private BufferedImage deepCopy(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}
