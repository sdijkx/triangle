package nl.mh.test.robot.calibration;

import nl.mh.test.robot.domain.Configuration;
import nl.mh.test.robot.domain.Coordinates;
import nl.mh.test.robot.domain.PhotoCoordinates;
import nl.mh.test.robot.domain.Robot;
import nl.mh.test.robot.util.GradientDescent;
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
 * Created by Marc on 21-6-2016.
 */
public class CameraCalibrator {
    private static Logger LOG = LoggerFactory.getLogger(CameraCalibrator.class);
    final private Robot robot;
    final private Configuration config;
    final CenterProcessor centerProcessor;

    public CameraCalibrator(Robot robot, Configuration config) {
        this.robot = robot;
        this.config = config;
        this.centerProcessor = new CenterProcessor(robot, config);
    }

    public void calibrate() {
        robot.getPlantImagePosition(Coordinates.HOME);
        config.setCalibrationMode(true);
        gotoCenter();
        calculatePointDifference();
        config.setCalibrationMode(false);
        gotoCenter();
        test();
    }

    private void test() {
        Coordinates start = robot.getFlatPosition();
        gotoCenter();
        robot.moveTo(start.add(222, -222));
        gotoCenter();

    }

    private void calculatePointDifference() {
        double[][] xTraining = new double[22][3];
        double[] xResults = new double[22];
        double[][] yTraining = new double[22][3];
        double[] yResults = new double[22];
        Coordinates start = robot.getFlatPosition();
        for (int i = 0; i < 22; i = i + 1) {
            int x = (i - 11) * 50;
            int y = (i - 11) * 50;
            LOG.info("step: {}, move to {}, {}", i, x, y);
            robot.moveTo(start.add(x, y));
            PhotoCoordinates photoCoordinates = centerProcessor.findSpot();
            addTrainingsRow(xTraining, xResults, -(photoCoordinates.getPixelX()- (config.getCameraImageWidth() / 2) + .5), x, i,photoCoordinates.getPixelX());
            addTrainingsRow(yTraining, yResults, -(photoCoordinates.getPixelY()- (config.getCameraImageHeight() / 2) + .5), y, i, photoCoordinates.getPixelY());
        }
        double[] theta = new double[]{1, 2.3, 0};


        GradientDescent gdX = new GradientDescent(xTraining, xResults, theta, 0.00001, 100000, 0.0000000001);
        GradientDescent gdY = new GradientDescent(yTraining, yResults, theta, 0.00001, 100000, 0.0000000001);
        double[] thetaX = gdX.computeTheta();
        double[] thetaY = gdY.computeTheta();

        LOG.info("xTraining: {}", Arrays.deepToString(xTraining));
        LOG.info("xResults: {}", xResults);
        LOG.info("thetaX: {}", thetaX);
        LOG.info("yTraining: {}", Arrays.deepToString(yTraining));
        LOG.info("yResults: {}", yResults);
        LOG.info("thetaY: {}", thetaY);

        store("ThetaX.conf", thetaX);
        store("ThetaY.conf", thetaY);

    }

    private void store(String label, double[] theta) {
        List<String> lines = Arrays.stream(theta)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());

        Path file = Paths.get("config/" + label);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void addTrainingsRow(double[][] training, double[] results, double in, double out, int index, double raw) {
        training[index] = config.getCalculatedAttributes(in);
        results[index] = out;
        LOG.info("add at index {}, found: {}, training {}, ----result---> {},---raw{}", index, in, training[index], results[index], raw);
    }


    public void gotoCenter() {
        Coordinates firstSample = centerProcessor.findCenter();
        Coordinates robotStart = robot.getFlatPosition();
//        if (firstSample.equals(robotStart) || firstSample.getX() == robotStart.getX() || firstSample.getY() == robotStart.getY()) {
//            LOG.warn("CameraCalibrator stopted. Spot is at least on one axis already on the middle. first: {}, robot: {}", firstSample, robotStart);
//            throw new IllegalStateException("Sport already in the middle");
//        }
        for (int i = 0; i < 10; i++) {
            Coordinates coordinates = centerProcessor.findCenter();
            LOG.info("Step: {}, spot found on: {}", i, coordinates);
            if (coordinates.equals(robot.getFlatPosition())) {
                LOG.info("Robot on right position.");
                break;
            }
            robot.moveTo(coordinates);
        }
        LOG.info("center found.");
    }


}
