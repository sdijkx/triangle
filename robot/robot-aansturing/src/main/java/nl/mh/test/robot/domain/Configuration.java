package nl.mh.test.robot.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.stream.IntStream;

/**
 * Created by Marc on 17-6-2016.
 */
public class Configuration {
    private static Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private static Coordinates WATER_RESERVOIR = new Coordinates(0, 300);
    private static Coordinates TIPS = new Coordinates(1700, 700);
    private static Coordinates RUBBISH_BIN = new Coordinates(3600, 4000);
    private static Coordinates START_PLANT_SCAN = new Coordinates(600, 4000);
    private static Coordinates CALIBRATION = new Coordinates(0, 0);
    private static int Z_SET_TIP = 1150;
    private static int Z_SET_DROP = 1150;
    private static int Z_GET_WATER = 1150;
    private static int PIPET_RELEASE = 0;
    private static int PIPET_PUSH = 0;
    private static int PIPET_NEUTRAL = 0;
    private static int X_MAX = 20000;
    private static int Y_MAX = 25000;
    private static int Z_MAX = 20000;
    private static int PIPET_MAX = 0;
    public static int CAMERA_IMAGE_WIDTH = 1600;
    public static int CAMERA_IMAGE_HEIGHT = 1200;
    private static final int INITIAL_X_DIV = -40;
    private static final int INITIAL_Y_DIV = -400;
    private static double STEPCONSTANTE = 1;
    private double[] thetaX;
    private double[] thetaY;
    private int xDivCameraPipet;
    private int yDivCameraPipet;
    private boolean calibrationMode = false;
    private String configFolder = "config";


    public Coordinates getWaterReservoir() {
        return WATER_RESERVOIR;
    }

    public Coordinates getTipsPlace() {
        return TIPS;
    }

    public Coordinates getRubbishBin() {
        return RUBBISH_BIN;
    }

    public Coordinates getStartPlantScan() {
        return START_PLANT_SCAN;
    }

    public Coordinates getCalibration() {
        return CALIBRATION;
    }

    public int getzSetTip() {
        return Z_SET_TIP;
    }

    public int getzSetDrop() {
        return Z_SET_DROP;
    }

    public int getzGetWater() {
        return Z_GET_WATER;
    }

    public int getPipetRelease() {
        return PIPET_RELEASE;
    }

    public int getPipetPush() {
        return PIPET_PUSH;
    }

    public int getPipetNeutral() {
        return PIPET_NEUTRAL;
    }

    public int getxMax() {
        return X_MAX;
    }

    public int getyMax() {
        return Y_MAX;
    }

    public int getzMax() {
        return Z_MAX;
    }

    public int getPipetMax() {
        return PIPET_MAX;
    }

    public static int getCameraImageWidth() {
        return CAMERA_IMAGE_WIDTH;
    }

    public static int getCameraImageHeight() {
        return CAMERA_IMAGE_HEIGHT;
    }

    public int getPixelStepX(int pixelX) {
        readThetaX();
        double fromCenter = (pixelX - (CAMERA_IMAGE_WIDTH / 2) + .5);
        double correctecPixel = (thetaX == null || calibrationMode) ? fromCenter : calculateCorrection(fromCenter, thetaX);
        LOG.info("correctecPixel x:{}", correctecPixel);
        return (int) correctecPixel;
    }

    public int getPixelStepY(int pixelY) {
        readThetaY();
        double fromCenter = (pixelY - (CAMERA_IMAGE_HEIGHT / 2) + .5);
        double correctecPixel = (thetaY == null || calibrationMode) ? fromCenter : calculateCorrection(fromCenter, thetaY);
        LOG.info("correctecPixel y:{}", correctecPixel);
        return (int) correctecPixel;
    }


    private double calculateCorrection(double in, double[] theta) {
        double[] attributes = getCalculatedAttributes(in);
        LOG.info("correctecPixel in:{}", in);
        return IntStream.range(0, theta.length)
                .mapToDouble(i -> (attributes[i] * theta[i])).sum();

    }

    private void readThetaX() {
        if (thetaX != null) return;
        try {
            thetaX = Files.readAllLines(new File(configFolder +"/ThetaX.conf").toPath(), Charset.defaultCharset())
                    .stream()
                    .mapToDouble(Double::valueOf).toArray();
            LOG.info("thetaX: {}", thetaX);
        } catch (IOException e) {
            //
        }
    }


    private void readThetaY() {
        if (thetaY != null) return;
        try {
            thetaY = Files.readAllLines(new File(configFolder +"/ThetaX.conf").toPath(), Charset.defaultCharset())
                    .stream()
                    .mapToDouble(Double::valueOf).toArray();
            LOG.info("thetaY: {}", thetaY);
        } catch (IOException e) {
            //
        }
    }

    public int getxDivCameraPipet() {
        if (xDivCameraPipet == 0) return readXDivCameraPipet();
        return xDivCameraPipet;
    }

    private int readXDivCameraPipet() {
        try {
            return Files.readAllLines(new File(configFolder +"/xDiv.conf").toPath(), Charset.defaultCharset())
                    .stream()
                    .mapToInt(Integer::valueOf)
                    .findFirst().getAsInt();
        } catch (IOException e) {
            return INITIAL_X_DIV;
        }
    }


    private int readYDivCameraPipet() {
        try {
            return Files.readAllLines(new File(configFolder +"/yDiv.conf").toPath(), Charset.defaultCharset())
                    .stream()
                    .mapToInt(Integer::valueOf)
                    .findFirst().getAsInt();
        } catch (IOException e) {
            return INITIAL_Y_DIV;
        }
    }

    public int getyDivCameraPipet() {
        return yDivCameraPipet;
    }

    public double[] getCalculatedAttributes(double in) {
        double[] row = new double[3];
        row[0] = 1.0;
        row[1] = in;
        row[2] = (Math.pow(in, 2));
        return row;
    }

    public void setCalibrationMode(boolean calibrationMode) {
        this.calibrationMode = calibrationMode;
    }

    public void setConfigFolder(String configFolder) {
        this.configFolder = configFolder;
    }
}
