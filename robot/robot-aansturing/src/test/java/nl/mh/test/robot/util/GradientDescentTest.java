package nl.mh.test.robot.util;

import org.testng.annotations.Test;

/**
 * Created by Marc on 7-10-2016.
 */
public class GradientDescentTest {


    @Test
    public void getCam() {

//        double[][] xTraining = {{1.0, -0.144375, 0.0166753125}, {1.0, -0.131875, 0.0139128125}, {1.0, -0.118125, 0.0111628125}, {1.0, -0.106875, 0.0091378125}, {1.0, -0.095625, 0.0073153125}, {1.0, -0.079375, 0.0050403125}, {1.0, -0.064375, 0.0033153125}, {1.0, -0.051875, 0.0021528125}, {1.0, -0.038125, 0.0011628125}, {1.0, -0.026875, 5.778125E-4}, {1.0, -0.013125, 1.378125E-4}, {1.0, -6.25E-4, 3.125E-7}, {1.0, 0.014375, 1.653125E-4}, {1.0, 0.026875, 5.778125E-4}, {1.0, 0.188125, 0.0283128125}, {1.0, 0.054375, 0.0023653125}, {1.0, 0.065625, 0.0034453125}, {1.0, 0.080625, 0.0052003125}, {1.0, 0.254375, 0.0517653125}, {1.0, 0.106875, 0.0091378125}, {1.0, 0.116875, 0.0109278125}, {1.0, 0.133125, 0.0141778125}};
//        double[] xResults = {-275.0, -250.0, -225.0, -200.0, -175.0, -150.0, -125.0, -100.0, -75.0, -50.0, -25.0, 0.0, 25.0, 50.0, 75.0, 100.0, 125.0, 150.0, 175.0, 200.0, 225.0, 250.0};


        double[][] xTraining = {{1.0, -232.5, 54056.25}, {1.0, -211.5, 44732.25}, {1.0, -191.5, 36672.25}, {1.0, -170.5, 29070.25}, {1.0, -151.5, 22952.25}, {1.0, -127.5, 16256.25}, {1.0, -106.5, 11342.25}, {1.0, -85.5, 7310.25}, {1.0, -64.5, 4160.25}, {1.0, -42.5, 1806.25}, {1.0, -21.5, 462.25}, {1.0, -0.5, 0.25}, {1.0, 20.5, 420.25}, {1.0, 45.5, 2070.25}, {1.0, 64.5, 4160.25}, {1.0, 84.5, 7140.25}, {1.0, 106.5, 11342.25}, {1.0, 129.5, 16770.25}, {1.0, 274.5, 75350.25}, {1.0, 168.5, 28392.25}, {1.0, 190.5, 36290.25}, {1.0, 212.5, 45156.25}};
        double[] xResults = {-550.0, -500.0, -450.0, -400.0, -350.0, -300.0, -250.0, -200.0, -150.0, -100.0, -50.0, 0.0, 50.0, 100.0, 150.0, 200.0, 250.0, 300.0, 350.0, 400.0, 450.0, 500.0};



        // double[] theta = new double[]{-35, 1370, -2600};
        double[] theta = new double[]{1, 2.3, 0};
        GradientDescent gd = new GradientDescent(xTraining, xResults, theta, 0.00001, 10000000, 0.0000000001);

        double[] newTheta = gd.computeTheta();

        String kip = "";
    }


}