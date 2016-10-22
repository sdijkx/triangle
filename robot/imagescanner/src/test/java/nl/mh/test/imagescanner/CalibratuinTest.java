package nl.mh.test.imagescanner;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Marc on 2-9-2016.
 */
public class CalibratuinTest {

    @Test
    public void getY(){
        Calibratuin c = new Calibratuin();
        int i = 5;
        double returnValue = (1 * Math.pow(i, 2)) + (1 * i) ;

        assertEquals( returnValue , c.getY(i), 0.00001);
    }


    @Test
    public void getCam() {

        double[][] xTraining = {{1.0, -0.144375, 0.0166753125}, {1.0, -0.131875, 0.0139128125}, {1.0, -0.118125, 0.0111628125}, {1.0, -0.106875, 0.0091378125}, {1.0, -0.095625, 0.0073153125}, {1.0, -0.079375, 0.0050403125}, {1.0, -0.064375, 0.0033153125}, {1.0, -0.051875, 0.0021528125}, {1.0, -0.038125, 0.0011628125}, {1.0, -0.026875, 5.778125E-4}, {1.0, -0.013125, 1.378125E-4}, {1.0, -6.25E-4, 3.125E-7}, {1.0, 0.014375, 1.653125E-4}, {1.0, 0.026875, 5.778125E-4}, {1.0, 0.188125, 0.0283128125}, {1.0, 0.054375, 0.0023653125}, {1.0, 0.065625, 0.0034453125}, {1.0, 0.080625, 0.0052003125}, {1.0, 0.254375, 0.0517653125}, {1.0, 0.106875, 0.0091378125}, {1.0, 0.116875, 0.0109278125}, {1.0, 0.133125, 0.0141778125}};
        double[] xResults = {-275.0, -250.0, -225.0, -200.0, -175.0, -150.0, -125.0, -100.0, -75.0, -50.0, -25.0, 0.0, 25.0, 50.0, 75.0, 100.0, 125.0, 150.0, 175.0, 200.0, 225.0, 250.0};
        double[] theta = new double[3];

        GradientDescent gd = new GradientDescent(xTraining, xResults, theta, 0.00001, 10000000, 0.01);

        double[] newTheta = gd.computeTheta();

        String kip = "";
    }

}
