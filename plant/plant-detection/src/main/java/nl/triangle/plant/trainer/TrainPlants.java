package nl.triangle.plant.trainer;

import nl.triangle.plant.classifier.ImageClassifierConfiguration;
import nl.triangle.plant.classifier.ImageClassifierSVM;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.block.BlockStreamConfiguration;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.ComputeHistogram;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptor;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptorFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by steven on 06-12-15.
 */
public class TrainPlants {

    public static void main(String[] args) throws IOException {
        TrainPlants main = new TrainPlants();
        main.trainSVM();
    }

    private void trainSVM() throws IOException {

        BlockStreamConfiguration blockStreamConfiguration = new BlockStreamConfiguration(8,3,4, new ComputeHistogram());
        ImageDescriptor hogDescriptor = ImageDescriptorFactory.newInstance().createHogDescriptor(blockStreamConfiguration);
        ImageClassifierConfiguration configuration = new ImageClassifierConfiguration(hogDescriptor, blockStreamConfiguration.getDescriptorSize());

        Path model = Paths.get("trainingset", "plants", "model-svm.eg");
        Path trainingFiles = Paths.get("trainingset", "plants");
        ImageClassifierTrainerSVM trainer = new ImageClassifierTrainerSVM(configuration, trainingFiles, "positive-svm", "negative-svm", model);
        trainer.train();
        trainer.writeClassifier();
        System.out.println("Classifier trained");

        ImageClassifierSVM plantClassifier = new ImageClassifierSVM(configuration, model);

        Path validation = Paths.get("trainingset", "plants", "positive-svm");

        ImageClassificationStream.applyClassifier(plantClassifier, Paths.get("trainingset", "plants", "positive-svm")).forEach(System.out::println);
        ImageClassificationStream.applyClassifier(plantClassifier, Paths.get("trainingset", "plants", "negative-svm")).forEach(System.out::println);;
        ImageClassificationStream.applyClassifier(plantClassifier, Paths.get("trainingset", "plants", "validation-svm")).limit(500).filter(classification -> classification.isClassified()).count();

    }


}
