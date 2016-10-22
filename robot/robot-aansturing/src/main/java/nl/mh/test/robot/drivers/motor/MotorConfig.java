package nl.mh.test.robot.drivers.motor;

/**
 * Created by Marc on 26-11-2015.
 */
public class MotorConfig {
    private String compoort;
    private int baudrate;

    public MotorConfig(String compoort, int baudrate) {
        this.compoort = compoort;
        this.baudrate = baudrate;
    }

    public String getCompoort() {
        return compoort;
    }

    public int getBaudrate() {
        return baudrate;
    }
}
