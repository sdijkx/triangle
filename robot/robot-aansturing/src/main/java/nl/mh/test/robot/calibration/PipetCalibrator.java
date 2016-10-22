package nl.mh.test.robot.calibration;

import nl.mh.test.robot.domain.Configuration;
import nl.mh.test.robot.domain.Coordinates;
import nl.mh.test.robot.domain.Robot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Marc on 27-9-2016.
 */
public class PipetCalibrator {
    private static Logger LOG = LoggerFactory.getLogger(PipetCalibrator.class);
    final private Robot robot;
    final private Configuration config;
    final CenterProcessor centerProcessor;

    public PipetCalibrator(Robot robot, Configuration config) {
        this.robot = robot;
        this.config = config;
        this.centerProcessor = new CenterProcessor(robot, config);
    }

    public void calibrate(){
        Coordinates start = robot.getFlatPosition();
        LOG.info("start:{}", start);
        moveToCameraPosition();
        LOG.info("Find center:{}", start);
        Coordinates photoCoordinates = centerProcessor.findCenter();
        LOG.info("detected mis configuration:{}", photoCoordinates);

        Coordinates cameraPos = robot.getFlatPosition();
        int xDiv = start.getX() - cameraPos.getX();
        store("xDiv.conf", xDiv);

        int yDiv = start.getY() - cameraPos.getY();
        store("yDiv.conf", yDiv);
    }

    private void moveToCameraPosition() {
        Coordinates start = robot.getFlatPosition();
        LOG.info("start.add(- config.getxDivCameraPipet(), -config.getxDivCameraPipet()):{}", start.add(- config.getxDivCameraPipet(), -config.getxDivCameraPipet()));
        robot.moveTo(start.add(- config.getxDivCameraPipet(), -config.getxDivCameraPipet()));
    }

    private void store(String label, int div) {
        List<String> lines = Arrays.asList(new String[]{String.valueOf(div)});

        Path file = Paths.get("config/" + label);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


}
