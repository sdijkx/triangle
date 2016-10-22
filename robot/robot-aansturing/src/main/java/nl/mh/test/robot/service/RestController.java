package nl.mh.test.robot.service;

import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import nl.mh.test.robot.calibration.CameraCalibrator;
import nl.mh.test.robot.calibration.PipetCalibrator;
import nl.mh.test.robot.domain.Configuration;
import nl.mh.test.robot.domain.Coordinates;
import nl.mh.test.robot.domain.Robot;
import nl.mh.test.robot.domain.RobotFactory;
import nl.mh.test.robot.drivers.camera.RaspberryCamera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import static spark.Spark.*;

/**
 * Created by Marc on 17-6-2016.
 */
public class RestController {
    private static Logger LOG = LoggerFactory.getLogger(RestController.class);
    public static void main(String[] args) {
        Robot robot = RobotFactory.createRobot();

        get("/hello", (request, response) -> {
            return "hello......";
        });

        get("/robotrun", (request, response) -> {
            robot.moveHome();
            robot.getTipsImage(Coordinates.HOME);
            robot.getTipsImage(new Coordinates(-30, -30));
            robot.getTip();
            robot.getWater();
            robot.getPlantImagePosition(Coordinates.HOME);
            robot.getPlantImagePosition(new Coordinates(-400, -200));
            robot.getTip();
            robot.throwPipet();
            return "ok.................";
        });


        get("tuneX/:steps", (request, response) -> {
            int steps = Integer.parseInt(request.params(":steps"));
            robot.tuneX(steps);
            return "done tune x................";
        });

        get("tuneY/:steps", (request, response) -> {
            int steps = Integer.parseInt(request.params(":steps"));
            robot.tuneY(steps);
            return "done tune y................";
        });

        get("tuneZ/:steps", (request, response) -> {
            int steps = Integer.parseInt(request.params(":steps"));
            robot.tuneZ(steps);
            return "done tune z................";
        });


        get("/getPlantimage", (request, response) -> {
            robot.moveHome();
            robot.getPlantImagePosition(Coordinates.HOME);
            return "ok.................";
        });


        get("/photo", (request, response) -> {
            long start = System.currentTimeMillis();
            JPEGImageReader reader = new JPEGImageReader(new JPEGImageReaderSpi());

            ImageIO.scanForPlugins();
            IIORegistry.getDefaultInstance().registerApplicationClasspathSpis();
            Iterator<ImageReader> ir = ImageIO.getImageReadersByFormatName("jpg");
            while(ir.hasNext()) {
                ImageReader r = ir.next();
                System.out.println("can read raster: " + r.canReadRaster());
                System.out.println(r);
            }



            RaspberryCamera camera = new RaspberryCamera();

//            BufferedImage im = ImageIO.read(new File("/tmp/image_2075979611163123655.jpg"));

            File file = camera.createPhoto().getAbsoluteFile();
            BufferedImage im = ImageIO.read(file);

            LOG.info("process image {} x {}", im.getWidth(), im.getHeight() );

            return "Photo.....: " + (System.currentTimeMillis() - start);
        });


        get("/calibrateCamera", (request, response) -> {
            CameraCalibrator calibration = new CameraCalibrator(robot, new Configuration());
            calibration.calibrate();

            return "CameraCalibrator.....: " ;
        });

        get("/calibratePipet", (request, response) -> {
            PipetCalibrator calibration = new PipetCalibrator(robot, new Configuration());
            calibration.calibrate();

            return "PipetCalibrator.....: " ;
        });
    }
}
