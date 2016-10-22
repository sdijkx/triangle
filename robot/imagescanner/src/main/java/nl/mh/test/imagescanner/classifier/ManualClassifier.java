package nl.mh.test.imagescanner.classifier;

import javax.swing.*;

/**
 * Created by Marc on 20-11-2015.
 */
public class ManualClassifier  extends JFrame {

    public ManualClassifier() {
        setTitle("My Empty Frame");
        setSize(300,200); // default size is 0,0
        setLocation(10,200); // default is 0,0 (top left corner)
    }

    public static void main(String[] args) {
        JFrame f = new ManualClassifier();
        f.show();
    }
}

