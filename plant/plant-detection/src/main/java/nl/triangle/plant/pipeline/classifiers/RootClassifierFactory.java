package nl.triangle.plant.pipeline.classifiers;

import nl.triangle.plant.classifier.ImageClassifierConfiguration;
import nl.triangle.plant.classifier.ImageClassifierSVM;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptor;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptorFactory;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.block.BlockStreamConfiguration;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.ComputeHistogram;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by steven on 11-07-16.
 */
public class RootClassifierFactory {
    public ImageClassifierSVM create() throws IOException {
        ImageClassifierConfiguration imageClassifierConfiguration = createConfiguration();
        ImageClassifierSVM imageClassifier =  new ImageClassifierSVM(imageClassifierConfiguration, Paths.get("trainingset", "roots", "model-root-svm.eg"));
        return imageClassifier;
    }

    public ImageClassifierConfiguration createConfiguration() throws IOException {
        BlockStreamConfiguration blockStreamConfiguration = new BlockStreamConfiguration(5, 2, 6, new ComputeHistogram());
        ImageDescriptor rootDescriptor = ImageDescriptorFactory.newInstance().createRootDescriptor(blockStreamConfiguration);
        ImageClassifierConfiguration imageClassifierConfiguration = new ImageClassifierConfiguration(rootDescriptor, blockStreamConfiguration.getDescriptorSize(), 20, 60);
        return  imageClassifierConfiguration;
    }

}
