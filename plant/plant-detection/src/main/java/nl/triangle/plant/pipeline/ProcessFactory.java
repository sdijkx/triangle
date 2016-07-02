package nl.triangle.plant.pipeline;

import nl.triangle.plant.classifier.ImageClassifier;
import nl.triangle.plant.classifier.ImageClassifierConfiguration;
import nl.triangle.plant.classifier.ImageClassifierSVM;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptor;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptorFactory;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.block.BlockStreamConfiguration;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.ComputeHistogram;
import nl.triangle.plant.pipeline.data.ClassifiedRootImage;
import nl.triangle.plant.pipeline.data.RootImage;
import nl.triangle.plant.pipeline.data.RootSkeleton;
import nl.triangle.plant.pipeline.dto.DropModel;
import nl.triangle.plant.pipeline.dto.ProcessResult;
import nl.triangle.plant.pipeline.dto.RootModel;
import nl.triangle.plant.pipeline.processors.DetermineDropLocation;
import nl.triangle.plant.pipeline.processors.FindRootSkeleton;
import nl.triangle.plant.pipeline.processors.RootDetection;
import nl.triangle.plant.pipeline.processors.SlidingWindow;
import nl.triangle.plant.pipeline.data.DropLocation;
import nl.triangle.plant.pipeline.dto.PlantModel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by steven on 27-06-16.
 */
public class ProcessFactory {

    public Process create() {
        return (image) -> {
            return image
                    .map(findPlants())
                    .map(findRoots())
                    .map(detectRoots())
                    .map(skeletonize())
                    .map(determineDroplocation())
                    .map(collectResults())
                    .orElseThrow(() -> new IllegalStateException());
        };
    }

    public Process createAnother() {
        return (image) -> {
            return image
                    .map(findPlants())
                    .map(rootImageStream ->  {
                        return rootImageStream.flatMap(findRoots2())
                            .map(rootImage -> new ClassifiedRootImage(rootImage))
                            .map(classifiedRootImage -> new RootSkeleton(classifiedRootImage))
                            .map(rootSkeleton -> new DropLocation(rootSkeleton));
                    })
                    .map(collectResults())
                    .orElseThrow(() -> new IllegalStateException());
        };
    }


    private Function<Stream<DropLocation>, ProcessResult> collectResults() {
        return (dropLocationStream) -> {
            List<PlantModel> plantModelList = new ArrayList<>();
            List<DropModel> dropModelList = new ArrayList<>();
            List<RootModel> rootModelList = new ArrayList<>();
            dropLocationStream.forEach(dropLocation -> {
                dropModelList.add(toDropModel(dropLocation));
                plantModelList.add(toPlantModel(dropLocation.getRootSkeleton()));
                rootModelList.add(toRootModel(dropLocation.getRootSkeleton().getClassifiedRootImage().getRootImage()));
            });
            return new ProcessResult("OK", dropModelList, plantModelList, rootModelList);

        };
    }

    private Function<Stream<RootSkeleton>, Stream<DropLocation>> determineDroplocation() {
        return rootSkeletonStream -> {
            DetermineDropLocation determineDropLocation = new DetermineDropLocation();
            List<DropLocation> dropLocationList = determineDropLocation.processRootSkeletonList(rootSkeletonStream.collect(Collectors.toList()));
            return dropLocationList.stream();
        };
    }

    private Function<Stream<ClassifiedRootImage>, Stream<RootSkeleton>> skeletonize() {
        return classifiedRootImageStream -> {
            FindRootSkeleton rootSkeletonize = new FindRootSkeleton();
            List<RootSkeleton> rootSkeletonList = rootSkeletonize.processRootImages(classifiedRootImageStream.collect(Collectors.toList()));
            return rootSkeletonList.stream();
        };
    }

    private Function<Stream<RootImage>, Stream<ClassifiedRootImage>> detectRoots() {
        return (rootImageStream) -> {
            RootDetection rootDetection = new RootDetection();
            List<ClassifiedRootImage> classifiedRootImages = rootDetection.processRootImages(rootImageStream.collect(Collectors.toList()));
            return classifiedRootImages.stream();
        };
    }

    private Function<Stream<RootImage>, Stream<RootImage>> findRoots() {
        return rootImageStream -> {
            return rootImageStream.flatMap(rootImage -> {
                try {
                    BlockStreamConfiguration blockStreamConfiguration = new BlockStreamConfiguration(5, 2, 6, new ComputeHistogram());
                    ImageDescriptor hogDescriptor = ImageDescriptorFactory.newInstance().createHogDescriptor(blockStreamConfiguration);
                    ImageClassifierConfiguration imageClassifierConfiguration = new ImageClassifierConfiguration(hogDescriptor, blockStreamConfiguration.getDescriptorSize());
                    ImageClassifierSVM imageClassifier = null;
                    imageClassifier = new ImageClassifierSVM(imageClassifierConfiguration, Paths.get("trainingset", "roots", "model-root-svm.eg"));
                    SlidingWindow slidingWindow = new SlidingWindow(rootImage.getBufferedImage(), imageClassifier, 20, 60, 10);
                    return slidingWindow.process().stream();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            });
        };
    }

    private Function<RootImage, Stream<RootImage>> findRoots2() {
        return rootImage -> {
            try {
                BlockStreamConfiguration blockStreamConfiguration = new BlockStreamConfiguration(5, 2, 6, new ComputeHistogram());
                ImageDescriptor hogDescriptor = ImageDescriptorFactory.newInstance().createHogDescriptor(blockStreamConfiguration);
                ImageClassifierConfiguration imageClassifierConfiguration = new ImageClassifierConfiguration(hogDescriptor, blockStreamConfiguration.getDescriptorSize());
                ImageClassifierSVM imageClassifier = null;
                imageClassifier = new ImageClassifierSVM(imageClassifierConfiguration, Paths.get("trainingset", "roots", "model-root-svm.eg"));
                SlidingWindow slidingWindow = new SlidingWindow(rootImage.getBufferedImage(), imageClassifier, 20, 60, 10);
                return slidingWindow.process().stream();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        };
    }


    private Function<BufferedImage, Stream<RootImage>> findPlants() {
        return image -> {
            try {
                BlockStreamConfiguration blockStreamConfiguration = new BlockStreamConfiguration(8, 3, 4, new ComputeHistogram());
                ImageDescriptor hogDescriptor = ImageDescriptorFactory.newInstance().createHogDescriptor(blockStreamConfiguration);
                ImageClassifierConfiguration imageClassifierConfiguration = new ImageClassifierConfiguration(hogDescriptor, blockStreamConfiguration.getDescriptorSize());
                ImageClassifier imageClassifier = new ImageClassifierSVM(imageClassifierConfiguration, Paths.get("trainingset", "plants", "model-svm.eg"));
                SlidingWindow slidingWindow = new SlidingWindow(image, imageClassifier, 200, 300, 30);
                List<RootImage> rootImages = slidingWindow.process();
                return rootImages.stream();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

    private RootModel toRootModel(RootImage rootImage) {
        RootModel rootModel = new RootModel();
        rootModel.setX(rootImage.getX());
        rootModel.setY(rootImage.getY());
        rootModel.setWidth(rootImage.getBufferedImage().getWidth());
        rootModel.setHeight(rootImage.getBufferedImage().getHeight());
        return rootModel;
    }

    private DropModel toDropModel(DropLocation dropLocation) {
        return new DropModel(dropLocation.getX(), dropLocation.getY(), dropLocation.getWeight());
    }

    private PlantModel toPlantModel(RootSkeleton rootSkeleton) {
        PlantModel plantModel = new PlantModel();
        plantModel.setBox(rootSkeleton.getTrendLine().getBox());
        plantModel.setX(rootSkeleton.getX());
        plantModel.setY(rootSkeleton.getY());
        return plantModel;
    }
}
