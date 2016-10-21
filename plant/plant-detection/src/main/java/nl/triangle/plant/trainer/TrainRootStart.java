package nl.triangle.plant.trainer;

import nl.triangle.plant.classifier.ImageClassifierConfiguration;
import nl.triangle.plant.classifier.ImageClassifierSVM;
import nl.triangle.plant.pipeline.classifiers.RootClassifierFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by steven on 13-06-16.
 */
public class TrainRootStart {

    public static void main(String[] args) throws IOException {
        Path model = Paths.get("trainingset", "roots", "model-root-svm.eg");
        Path trainingFiles = Paths.get("trainingset", "roots");

        ImageClassifierConfiguration imageClassifierConfiguration = new RootClassifierFactory().createConfiguration();

        ImageClassifierTrainerSVM imageClassifierTrainerSVM = new ImageClassifierTrainerSVM(imageClassifierConfiguration, trainingFiles, "positive", "negative", model);
        imageClassifierTrainerSVM.train();
        imageClassifierTrainerSVM.writeClassifier();

        ImageClassifierSVM plantClassifier = new ImageClassifierSVM(imageClassifierConfiguration, model);

        ImageClassificationStream.applyClassifier(plantClassifier, Paths.get("trainingset", "roots", "positive")).forEach(System.out::println);
        ImageClassificationStream.applyClassifier(plantClassifier, Paths.get("trainingset", "roots", "negative")).forEach(System.out::println);;
        ImageClassificationStream.applyClassifier(plantClassifier, Paths.get("trainingset", "roots", "validation")).limit(500).filter(classification -> classification.isClassified()).count();
    }
}
