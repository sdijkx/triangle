package nl.mh.test.robot.drivers.motor;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Marc on 5-5-2016.
 */
public class Pi4JDirection {
    private static Logger LOG = LoggerFactory.getLogger(Pi4JDirection.class);
    private final GpioPinDigitalOutput pin;
    private final PinState posiveState;

    public Pi4JDirection(GpioPinDigitalOutput pin, PinState posiveState) {
        this.pin = pin;
        this.posiveState = posiveState;
    }

    public void goNegative(){
        if(posiveState == PinState.HIGH) {
            pin.setState(PinState.LOW);
        } else {
            pin.setState(PinState.HIGH);
        }
    }

    public void goPositive(){
        if(posiveState == PinState.HIGH) {
            pin.setState(PinState.HIGH);
        } else {
            pin.setState(PinState.LOW);
        }
    }

    public void setDirection(int aantalStappen) {
        if(aantalStappen<0){
            LOG.debug("go negative");
            goNegative();
        }else{
            LOG.debug("go positive");
            goPositive();
        }
    }
}
