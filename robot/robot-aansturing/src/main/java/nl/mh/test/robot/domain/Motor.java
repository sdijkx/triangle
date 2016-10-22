package nl.mh.test.robot.domain;

import nl.mh.test.robot.domain.exception.PositionOverMaxException;
import nl.mh.test.robot.drivers.motor.MotorDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Marc on 26-11-2015.
 */
public class Motor {
    private static Logger LOG = LoggerFactory.getLogger(Motor.class);
    private String name;
    private AtomicInteger position = new AtomicInteger();
    private boolean positionKnown = false;
    private MotorDriver dirver;
    private int max = 0;

    public Motor(String name, MotorDriver driver, int max) {
        this.name = name;
        this.dirver = driver;
        this.max = max;
    }

    public void goHome(){
        LOG.debug("Motor: {} to 0", name);
        dirver.toZero();
        position.set(0);
        positionKnown = true;
    }

    public void toPosition(int positionToGo) {
        LOG.debug("Motor {} knows position: {}", name , positionKnown);
        throwExceptionWhenOverMax(positionToGo);
        if (!positionKnown) {
            goHome();
        }
        LOG.debug("Motor {} from position: {} to position {}", name, position, positionToGo);

        LOG.debug(name + " knows position: " + positionKnown);
        try {
            synchronized (this) {
                LOG.debug("action --> " + (positionToGo - position.get()));
                dirver.loop(positionToGo - position.get());
                position.set(positionToGo);
            }

        } catch (RuntimeException e) {
            positionKnown = false;
            throw e;
        }
    }

    private void throwExceptionWhenOverMax(int positionToGo) {
        if(positionToGo>max) {
            throw new PositionOverMaxException(String.format("Requested position [%d] > max [%d]", positionToGo, max));
        }
    }

    public int getPosition(){
        return position.get();
    }


}
