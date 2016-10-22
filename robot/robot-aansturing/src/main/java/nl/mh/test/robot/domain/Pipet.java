package nl.mh.test.robot.domain;

/**
 * Created by Marc on 24-11-2015.
 */
public class Pipet {
   final private Motor motor;
    final private int neutral;
    final private int push;
    final private int release;
    private boolean tip = false;

    public Pipet(Motor motor, int neutral, int push, int release) {
        this.motor = motor;
        this.neutral = neutral;
        this.push = push;
        this.release = release;
    }

    public void push(){
        motor.toPosition(push);
    }

    public void toNeutral(){
        motor.toPosition(neutral);
    }

    public void release(){
        motor.toPosition(release);
    }

}
