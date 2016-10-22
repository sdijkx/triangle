package nl.mh.test.robot.imagerecognition.loopstrategy;

import nl.mh.test.robot.imagerecognition.ImageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.InputStream;

/**
 * Created by Marc on 23-6-2016.
 */
public class FromCenterLoopStrategyTest {
    private static Logger LOG = LoggerFactory.getLogger(FromCenterLoopStrategyTest.class);

    //@Test
    public void testIterator() {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("cal2.jpg")) {
            BufferedImage baseImage = javax.imageio.ImageIO.read(in);
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            baseImage = op.filter(baseImage, null);

            FromCenterLoopStrategy strategy = new FromCenterLoopStrategy(baseImage, 20, 2);
            for (ImageData subImage : strategy) {
                LOG.info(String.format("x-> %s, y -> %s", subImage.getX(), subImage.getY()));
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
