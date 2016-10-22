package nl.mh.test.robot.domain;

import nl.mh.test.robot.drivers.camera.RaspberryCamera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by Marc on 14-6-2016.
 */
public class Robot {
    private static Logger LOG = LoggerFactory.getLogger(Robot.class);
    final private Motor motorX;
    final private Motor motorY;
    final private Motor motorZ;
    final private Pipet pipet;
    final private Configuration config;
    final private RaspberryCamera camera = new RaspberryCamera();

    public Robot(Motor motorX, Motor motorY, Motor motorZ, Pipet pipet, Configuration config) {
        this.motorX = motorX;
        this.motorY = motorY;
        this.motorZ = motorZ;
        this.pipet = pipet;
        this.config = config;
    }

    public Coordinates getFlatPosition() {
        return new Coordinates(motorX.getPosition(), motorY.getPosition());
    }

    public void moveHome() {
        motorZ.goHome();
        moveTo(Coordinates.HOME);
    }

    public void moveTo(Coordinates coordinates) {

        System.out.println("move to " + coordinates.getX() + ", " + coordinates.getY());
        motorZ.goHome();
//        motorX.toPosition(coordinates.getX());
//        motorY.toPosition(coordinates.getY());
        CompletableFuture moveX = CompletableFuture.supplyAsync(
                () -> {
                    motorX.toPosition(coordinates.getX());
                    return true;
                });

        CompletableFuture moveY = CompletableFuture.supplyAsync(
                () -> {
                    motorY.toPosition(coordinates.getY());
                    return true;
                });
        try {
            moveX.get();
            moveY.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void movePipetTo(Coordinates coordinates) {
        motorZ.goHome();
        moveTo(coordinates.add(config.getCalibration()));
    }

    public void getTipsImage(Coordinates variance) {
        moveTo(config.getTipsPlace().add(variance));
    }

    public void getTip() {
        motorZ.toPosition(config.getzSetTip());
        motorZ.goHome();
    }

    public void getWater() {
        LOG.info("getWater");
        movePipetTo(config.getWaterReservoir());
        LOG.info("go down");
        motorZ.toPosition(config.getzGetWater());
        LOG.info("back up");
        motorZ.goHome();
    }

    public void getPlantImagePosition(Coordinates variance) {
        moveTo(config.getStartPlantScan().add(variance));
    }

    public void throwPipet() {
        moveTo(config.getRubbishBin());

    }

    public Photo createPhoto() {
        return new Photo(camera.createPhoto(),
                new Coordinates(motorX.getPosition(), motorY.getPosition()));
    }

    public void tuneX(int steps) {
        tune(motorX, steps, "X - ");
    }

    public void tuneY(int steps) {
        tune(motorY, steps, "Y - ");
    }

    public void tuneZ(int steps) {
        tune(motorZ, steps, "Z - ");
    }

    private void tune(Motor motor, int steps, String label) {
        int toPos = motor.getPosition() + steps;
        System.out.println(label + "to: " + toPos);
        motor.toPosition(toPos);

    }

}
