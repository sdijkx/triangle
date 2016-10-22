package nl.mh.test.robot.domain;

import com.pi4j.io.gpio.*;
import nl.mh.test.robot.drivers.motor.MotorDriver;
import nl.mh.test.robot.drivers.motor.Pi4JDirection;
import nl.mh.test.robot.drivers.motor.Pi4JMotorDriver;

/**
 * Created by Marc on 17-6-2016.
 */
public class RobotFactory {
    // create gpio controller
    static final GpioController gpio = GpioFactory.getInstance();

    public static Robot createRobot() {
        Configuration config = new Configuration();
        Motor motorX = createMotorX(config);
        Motor motorY = createMotorY(config);
        Motor motorZ = createMotorZ(config);
        Pipet pipet = createPipet(config);
        return new Robot(motorX, motorY, motorZ, pipet, config);
    }

    private static Motor createMotorX(Configuration config) {
        MotorDriver driverY = createMotorDriver("X", RaspiPin.GPIO_00, RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04);
        return new Motor("X", driverY, config.getyMax());
    }

    private static Motor createMotorY(Configuration config) {
        MotorDriver driverX = createMotorDriver("Y", RaspiPin.GPIO_08, RaspiPin.GPIO_09, RaspiPin.GPIO_07, RaspiPin.GPIO_15);
        return new Motor("Y", driverX, config.getxMax());
    }


    private static Motor createMotorZ(Configuration config) {
        MotorDriver driverZ = createMotorDriver("Z", RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_14, RaspiPin.GPIO_10);
        return new Motor("Z", driverZ, config.getzMax());
    }

    private static Pipet createPipet(Configuration config) {
        MotorDriver driver = createMotorDriver("Pipet", RaspiPin.GPIO_22, RaspiPin.GPIO_23, RaspiPin.GPIO_24, RaspiPin.GPIO_27);
        Motor motor =  new Motor("Pipet.", driver, config.getPipetMax());
        return new Pipet(motor, config.getPipetNeutral(), config.getPipetPush(), config.getPipetRelease());
    }


    private static MotorDriver createMotorDriver(String motorLabel, Pin directionPin, Pin stepPin, Pin enabledPin, Pin inFieldPin) {
        GpioPinDigitalOutput pinDirection = gpio.provisionDigitalOutputPin(directionPin, motorLabel + "-Direction", PinState.LOW);
        GpioPinDigitalOutput pinStep = gpio.provisionDigitalOutputPin(stepPin, motorLabel + "-Step", PinState.LOW);
        GpioPinDigitalOutput pinEnabled = gpio.provisionDigitalOutputPin(enabledPin, motorLabel + "-Enabled", PinState.HIGH);
        GpioPinDigitalInput pinInField = gpio.provisionDigitalInputPin(inFieldPin, motorLabel + "-InField", PinPullResistance.PULL_DOWN);

        return new Pi4JMotorDriver(new Pi4JDirection(pinDirection, PinState.HIGH),
                pinStep,
                pinEnabled,
                pinInField);
    }
}
