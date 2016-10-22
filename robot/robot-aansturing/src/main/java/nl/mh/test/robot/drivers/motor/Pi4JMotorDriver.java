package nl.mh.test.robot.drivers.motor;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Marc on 5-5-2016.
 */
public class Pi4JMotorDriver implements MotorDriver {
    private static Logger LOG = LoggerFactory.getLogger(Pi4JMotorDriver.class);
    private final Pi4JDirection direction;
    private final GpioPinDigitalOutput step;
    private final GpioPinDigitalInput onField;
    private final GpioPinDigitalOutput enabled;


    public Pi4JMotorDriver(Pi4JDirection direction, GpioPinDigitalOutput step, GpioPinDigitalOutput enabled, GpioPinDigitalInput onField) {
        this.direction = direction;
        this.step = step;
        this.onField = onField;
        this.enabled = enabled;
    }

    public void toZero() {
        enable();
        direction.goNegative();
        LOG.debug(" --> GPIO PIN READ: " + onField + " = " + onField.getState());
        while (onField.getState() == PinState.HIGH) {
            oneStep();
            LOG.debug(" --> GPIO PIN READ: " + onField + " = " + onField.getState());
        }
        disable();
    }

    private void disable() {
        enabled.setState(true);
        LOG.debug(" --> GPIO PIN READ: " + enabled + " = " + enabled.getState());
    }

    private void enable() {
        enabled.setState(false);
       LOG.debug(" --> GPIO PIN READ: " + enabled + " = " + enabled.getState());
    }

    private void oneStep() {
//        System.out.println(" --> GPIO PIN READ: " + enabled + " = " + enabled.getState());
        try {
            step.setState(true); // Output high
//            System.out.println(" --> GPIO PIN READ: " + step + " = " + step.getState());
            sleep();

            step.setState(false);// Output low
//            System.out.println(" --> GPIO PIN READ: " + step + " = " + step.getState());
            sleep();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleep() throws InterruptedException {
        Thread.sleep(0, 500000);
    }

    public void loop(int aantalStappen) {
        enable();
        direction.setDirection(aantalStappen);
        for (int i = 0; i < Math.abs(aantalStappen); i++) {
            oneStep();
        }
        disable();
    }
}
