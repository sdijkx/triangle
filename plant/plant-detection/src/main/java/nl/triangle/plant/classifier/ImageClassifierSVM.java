package nl.triangle.plant.classifier;

import nl.triangle.plant.pipeline.dto.PlantImageModel;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.svm.PersistSVM;
import org.encog.ml.svm.SVM;
import org.encog.util.arrayutil.NormalizeArray;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class ImageClassifierSVM implements ImageClassifier {

    private static final double threshold = 0.5;
    private SVM svm;
    private ImageClassifierConfiguration classifierConfiguration;

    public ImageClassifierSVM(ImageClassifierConfiguration configuration, Path model) throws IOException {
        classifierConfiguration = configuration;
        PersistSVM persistSVM = new PersistSVM();
        svm = (SVM) persistSVM.read(new FileInputStream(model.toFile()));
    }

    public ImageClassifierSVM(ImageClassifierConfiguration configuration) {
        classifierConfiguration = configuration;
        this.svm = new SVM(classifierConfiguration.getInputSize(), true);
    }

    @Override
    public boolean classifiy(Optional<BufferedImage> image) {
        MLData input = getMlDataFromImage(image);
        MLData output = svm.compute(input);
        return output.getData(0) > threshold;
    }

    public double compute(Optional<BufferedImage> image) {
        MLData input = getMlDataFromImage(image);
        MLData output = svm.compute(input);
        return output.getData(0);
    }

    public MLData getMlDataFromImage(Optional<BufferedImage> image) {
        MLData mlData = classifierConfiguration.getImageDescriptor().compute(image);
        NormalizeArray normalizeArray = new NormalizeArray();
        double[] normalizedData = normalizeArray.process(mlData.getData());
        MLData normalizedMLData = new BasicMLData(normalizedData);
        return mlData;
    }

    public SVM getSvm() {
        return svm;
    }


    @Override
    public ImageClassifierConfiguration getConfiguration() {
        return classifierConfiguration;
    }
}
