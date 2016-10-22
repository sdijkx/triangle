package nl.mh.test.robot.domain.exception;

/**
 * Created by Marc on 17-6-2016.
 */
public class PositionOverMaxException extends RuntimeException {
    public PositionOverMaxException(String msg) {
        super(msg);
    }
}
