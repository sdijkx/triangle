package nl.mh.test.imagescanner;

import org.encog.engine.network.activation.ActivationElliott;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * Created by Marc on 17-11-2015.
 */
public class TrainGrovePipetScan {
    public static final String PRETRAINING_DIR = "\\pretraining";
    public static final String TEST_DIR = "\\test";
    public static final String TRUE_DIR = "\\true";
    public static final String FALSE_DIR = "\\false";
    public static final String TRAINING_DIR = "\\training";


//    private static String SET_NAME = "calibration-stip-preface";
    private static String SET_NAME = "calibration-stip";
//    private int pixels = 40;
    private int pixels = 28;

    private String pathToTypeSet = "C:\\Users\\Marc\\Documents\\WS\\datasets\\" + SET_NAME;

    private boolean preProcessing = true;

    public static final String NETWERK_FILE = "C:\\Users\\Marc\\Documents\\WS\\robot\\result\\netwerk\\"+ SET_NAME+ ".eg";

    public static void main(String args[]) {
        TrainGrovePipetScan scan = new TrainGrovePipetScan();
        try {
            scan.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void execute() throws Exception {

        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, pixels * pixels));
//        network.addLayer(new BasicLayer(new ActivationElliott(), true, 100));
//        network.addLayer(new BasicLayer(new ActivationElliott(), true, 50));
        network.addLayer(new BasicLayer(null, true, 1));
        network.getStructure().finalizeStructure();
        network.reset();

        MLDataSet trainingSet = generateTraining();
        MLTrain train = new ResilientPropagation(network, trainingSet);

        int epoch = 1;
        double min = 200;
        do {
            train.iteration();
            System.out
                    .println("Epoch #" + epoch + " Error:" + train.getError());
            if (min > train.getError()) {
                System.out.println("----------nieuw min--------");
                min = train.getError();
            }
            epoch++;
        } while (train.getError() > 0.01 && epoch < 20000);



        System.out.println("Saving network");
        EncogDirectoryPersistence.saveObject(new File(NETWERK_FILE), network);
        System.out.println("Saved");

        Files.walk(Paths.get(getTestsetPath()))
                .filter(Files::isRegularFile)
                .forEach(file -> printResult(network, file));
    }

    private String getTestsetPath() {
        if (preProcessing) {
            return pathToTypeSet + PRETRAINING_DIR;
        }
        return pathToTypeSet  + TEST_DIR;
    }

    private void printResult(BasicNetwork network, Path file) {
        try {
            MLData bla = network.compute(inputFromFile(file));
            if (bla.getData(0) > 0.80) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Path newdir = FileSystems.getDefault().getPath(getTestsetPath()+ TRUE_DIR);
                Files.move(file, newdir.resolve(String.valueOf(bla.getData(0)).substring(2, 4) + "_" + file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }
            if (bla.getData(0) < 0.087) {
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                Path newdir = FileSystems.getDefault().getPath(getTestsetPath() + FALSE_DIR);
                Files.move(file, newdir.resolve(String.valueOf(bla.getData(0)).substring(2, 4) + "_" + file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println(file.getFileName() + "-->>" + bla.getData(0));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public MLDataSet generateTraining() throws Exception {
        MLDataSet result = new BasicMLDataSet();
        Files.walk(Paths.get(pathToTypeSet + TRAINING_DIR + FALSE_DIR))
                .filter(Files::isRegularFile)
                .forEach(file -> addResult(result, file, 0));

        Files.walk(Paths.get(pathToTypeSet + TRAINING_DIR + TRUE_DIR))
                .filter(Files::isRegularFile)
                .forEach(file -> addResult(result, file, 1.0));

        return result;

    }

    private void addResult(MLDataSet result, Path file, double y) {
        BasicMLData input = inputFromFile(file);
        BasicMLData ideal = new BasicMLData(1);
        ideal.add(0, y);
        result.add(input, ideal);

    }

    private BasicMLData inputFromFile(Path file) {
        BasicMLData input = null;
        try (InputStream in = Files.newInputStream(file)) {
            BufferedImage image = javax.imageio.ImageIO.read(in);
            input = new BasicMLData(2500);
            int counter = 0;
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    double a = (image.getRGB(i, j) & 0xFF) / 256.0;
                    input.add(counter, (image.getRGB(i, j) & 0xFF) / 256.0);
                    counter++;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }


}
