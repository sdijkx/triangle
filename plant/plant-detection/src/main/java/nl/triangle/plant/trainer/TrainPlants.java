package nl.triangle.plant.trainer;

import nl.triangle.plant.classifier.ImageClassifierConfiguration;
import nl.triangle.plant.classifier.ImageClassifierSVM;
import nl.triangle.plant.pipeline.classifiers.PlantClassifierFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by steven on 06-12-15.
 */
public class TrainPlants {

    public static void main(String[] args) throws IOException {
        TrainPlants main = new TrainPlants();
        main.trainSVM();
    }

    private void trainSVM() throws IOException {

        ImageClassifierConfiguration configuration = new PlantClassifierFactory().createConfiguration();

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
