package nl.mh.test.robot.drivers.motor;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import org.zu.ardulink.Link;
import org.zu.ardulink.protocol.IProtocol;

/**
 * Created by Marc on 21-5-2016.
 */
public class ArdulinkMotorDriver  implements MotorDriver{
    private final ArdulinkMotorDirection direction;
    private final Link link;
    private final int pin;
    private final GpioPinDigitalInput onField;

    public ArdulinkMotorDriver(ArdulinkMotorDirection direction, Link link, int pin, GpioPinDigitalInput onField) {
        this.direction = direction;
        this.link = link;
        this.pin = pin;
        this.onField = onField;
    }

    public void toZero() {
        direction.goNegative();
        while (onField.getState() == PinState.HIGH) {
            oneStep();
        }

    }

    private void oneStep() {
        try {
            link.sendPowerPinSwitch(pin, IProtocol.HIGH);
            Thread.sleep(0,500000);// Wait 1/2 a ms

            link.sendPowerPinSwitch(pin, IProtocol.LOW);
            Thread.sleep(0,50000);// Wait 1/2 a ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loop(int aantalStappen) {
        direction.setDirection(aantalStappen);
        for (int i = 0; i < aantalStappen; i++) {
            oneStep();
        }

    }
}
