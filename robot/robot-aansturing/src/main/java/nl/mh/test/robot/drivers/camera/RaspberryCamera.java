package nl.mh.test.robot.drivers.camera;

import nl.mh.test.robot.domain.Camera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by Marc on 4-7-2016.
 */
public class RaspberryCamera implements Camera {
    private static final Logger LOG = LoggerFactory.getLogger(RaspberryCamera.class);
    @Override
    public File createPhoto() {


        try {
            File file = File.createTempFile("image_", ".jpg");
            LOG.debug("formats", ImageIO.getReaderFormatNames().length);
            LOG.debug("sudo fswebcam -r 1600x1200 " + file.getAbsolutePath());
            Runtime.getRuntime().exec("sudo fswebcam  -r 1600x1200 " + file.getAbsolutePath());
            LOG.debug("Photo created: {}, -> {}", file.exists(), file.getAbsolutePath() );
//            try {
//                LOG.warn("Sleep van 3 sec" );
//                Thread.sleep(3000);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
