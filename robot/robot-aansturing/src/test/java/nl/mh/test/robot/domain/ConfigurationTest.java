package nl.mh.test.robot.domain;

import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Marc on 7-10-2016.
 */
public class ConfigurationTest {

    @Test
    public void getPixelStepX() {
        Configuration configuration = new Configuration();
        configuration.setConfigFolder("target/test-classes/config");
        int pixel = configuration.getPixelStepX(100);
        assertEquals(-1703, pixel);
    }

    @Test
    public void getPixelStepY() {
        Configuration configuration = new Configuration();
        configuration.setConfigFolder("target/test-classes/config");
        int pixel = configuration.getPixelStepY(100);
        assertEquals(-1196, pixel);
    }
}
