package nl.triangle.plant.trainer;

import nl.triangle.plant.classifier.ImageClassifierConfiguration;
import nl.triangle.plant.classifier.ImageClassifierSVM;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.svm.PersistSVM;
import org.encog.ml.svm.training.SVMSearchTrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by steven on 05-07-15.
 */
public class ImageClassifierTrainerSVM {


    private final ImageClassifierSVM plantClassifierSVM;
    private final Path trainingFiles;
    private final String positiveTrainingset;
    private final String negativeTrainingset;
    private final Path model;

    public ImageClassifierTrainerSVM(ImageClassifierConfiguration configuration, Path trainingFiles, String positiveTrainingset, String negativeTrainingset, Path model) {
        this.positiveTrainingset = positiveTrainingset;
        this.negativeTrainingset = negativeTrainingset;
        this.plantClassifierSVM = new ImageClassifierSVM(configuration);
        this.trainingFiles = trainingFiles;
        this.model = model;
    }

    public void train() throws IOException {
        MLDataSet mlDataSet = new BasicMLDataSet();
        readSamples().forEach( mlDataPair -> mlDataSet.add(mlDataPair));
        SVMSearchTrain svmTrain = new SVMSearchTrain(plantClassifierSVM.getSvm(), mlDataSet);
        int epoch = 0;
        do {
            svmTrain.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + svmTrain.getError());
            epoch++;
        } while(svmTrain.getError() > 0.01 && epoch<10);
        svmTrain.finishTraining();
    }

    public void writeClassifier() throws IOException {
        PersistSVM persistSVM = new PersistSVM();
        persistSVM.save(Files.newOutputStream(model), plantClassifierSVM.getSvm());
    }

    private Stream<MLDataPair> readSamples() throws IOException {
        return Stream.concat(
            readPositiveSamples().map(mlData -> new BasicMLDataPair(mlData, new BasicMLData(new double[] {1.0}))),
            readNegativeSamples().map(mlData -> new BasicMLDataPair(mlData, new BasicMLData(new double[] {-1.0})))
        );
    }

    private Stream<MLData> readPositiveSamples() throws IOException {
        return Files.find(trainingFiles.resolve(positiveTrainingset), 1, this::isImage).map(this::readImageFile);
    }

    private Stream<MLData> readNegativeSamples() throws IOException {
        return Files.find(trainingFiles.resolve(negativeTrainingset), 1, this::isImage).map(this::readImageFile).limit(150);
    }

    private boolean isImage(Path path, BasicFileAttributes attr) {
        System.out.println("Read path " + path);
        return attr.isRegularFile() && path.toString().endsWith(".png");
    }

    private MLData readImageFile(Path file)  {
        BufferedImage image = null;
        return getMlDataFromImage(file);
    }

    private MLData getMlDataFromImage(Path file) {

        try {
            Optional<BufferedImage> image = Optional.of(ImageIO.read(Files.newInputStream(file)));
            return plantClassifierSVM.getMlDataFromImage(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
