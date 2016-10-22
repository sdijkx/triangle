package nl.mh.test.robot.drivers.motor;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import org.zu.ardulink.Link;
import org.zu.ardulink.protocol.IProtocol;

/**
 * Created by Marc on 21-5-2016.
 */
public class ArdulinkMotorDirection {
    private final Link link;
    private final int pin;
    private final int posiveState;

    public ArdulinkMotorDirection(Link link, int pin, int posiveState) {
        this.link = link;
        this.pin = pin;
        this.posiveState = posiveState;
    }

    public void goNegative(){
        if(posiveState == IProtocol.HIGH) {
            link.sendPowerPinSwitch(pin,IProtocol.LOW);
        } else {
            link.sendPowerPinSwitch(pin,IProtocol.HIGH);
        }
    }

    public void goPositive(){
        if(posiveState == IProtocol.HIGH) {
            link.sendPowerPinSwitch(pin,IProtocol.HIGH);
        } else {
            link.sendPowerPinSwitch(pin,IProtocol.LOW);
        }
    }

    public void setDirection(int aantalStappen) {
        if(aantalStappen<0){
            goNegative();
        }else{
            goPositive();
        }
    }
}
