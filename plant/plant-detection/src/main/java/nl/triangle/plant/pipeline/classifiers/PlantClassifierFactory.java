package nl.triangle.plant.pipeline.classifiers;

import nl.triangle.plant.classifier.ImageClassifier;
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
public class PlantClassifierFactory {
    public ImageClassifier create() throws IOException {
        ImageClassifierConfiguration imageClassifierConfiguration = createConfiguration();
        return new ImageClassifierSVM(imageClassifierConfiguration, Paths.get("trainingset", "plants", "model-svm.eg"));
    }

    public ImageClassifierConfiguration createConfiguration() {
        BlockStreamConfiguration blockStreamConfiguration = new BlockStreamConfiguration(8, 3, 4, new ComputeHistogram());
        ImageDescriptor hogDescriptor = ImageDescriptorFactory.newInstance().createPlantDescriptor(blockStreamConfiguration);
        return new ImageClassifierConfiguration(hogDescriptor, blockStreamConfiguration.getDescriptorSize(), 200, 300);
    }
}
