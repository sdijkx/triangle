package nl.triangle.plant.pipeline;

import nl.triangle.plant.classifier.*;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.block.BlockStreamConfiguration;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.ComputeHistogram;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptor;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.ImageDescriptorFactory;
import nl.triangle.plant.pipeline.data.RootImage;
import nl.triangle.plant.pipeline.dto.DropModel;
import nl.triangle.plant.pipeline.data.DropLocation;
import nl.triangle.plant.pipeline.data.RootSkeleton;
import nl.triangle.plant.pipeline.dto.PlantModel;
import nl.triangle.plant.pipeline.dto.ProcessResult;
import nl.triangle.plant.pipeline.dto.RootModel;
import nl.triangle.plant.pipeline.processors.SlidingWindow;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by steven on 14-05-16.
 */
public class ProcessImagePipeline implements Process {

    private static final Logger logger = Logger.getLogger(ProcessImagePipeline.class.getName());


    public ProcessResult process(Optional<BufferedImage> image) {
        try {
            List<RootImage> rootImagesList = new ArrayList<>();
            logger.info("plant segmentation processing");

//            ImageClassifierDummy imageClassifier = new ImageClassifierDummy();


            BlockStreamConfiguration blockStreamConfiguration = new BlockStreamConfiguration(8,3,4, new ComputeHistogram());
            ImageDescriptor hogDescriptor = ImageDescriptorFactory.newInstance().createHogDescriptor(blockStreamConfiguration);
            ImageClassifierConfiguration imageClassifierConfiguration = new ImageClassifierConfiguration(hogDescriptor, blockStreamConfiguration.getDescriptorSize());
            ImageClassifier imageClassifier = new ImageClassifierSVM(imageClassifierConfiguration, Paths.get("trainingset", "plants", "model-svm.eg"));

            SlidingWindow slidingWindow = new SlidingWindow(image.get(), imageClassifier, 200, 300, 30);


//            BlockStreamConfiguration blockStreamConfiguration = new BlockStreamConfiguration(5,4,12);
//            HOGDescriptor hogDescriptor = new HOGDescriptor(blockStreamConfiguration);
//            ImageClassifierConfiguration imageClassifierConfiguration = new ImageClassifierConfiguration(hogDescriptor, blockStreamConfiguration.getDescriptorSize());
//            ImageClassifierSVM imageClassifier = new ImageClassifierSVM(imageClassifierConfiguration, Paths.get("trainingset", "roots", "model-root-svm.eg"));
//            SlidingWindow slidingWindow = new SlidingWindow(image, imageClassifier, 20, 60, 10);

            List<RootImage> rootImagesListPhase1 = slidingWindow.process();
            logger.info("found " + rootImagesListPhase1.size() + " plants");

            List<RootImage> rootImagesListPhase2 = rootImagesListPhase1.stream().flatMap(rootImage -> {
                try {
                    List<RootImage> roots = segment(rootImage);


                    return roots.stream().map(rootStart -> new RootImage(rootStart.getBufferedImage(), rootImage.getX() + rootStart.getX(), rootImage.getY() + rootStart.getY()));
                } catch (IOException e) {
                    return Stream.empty();
                }
            }).collect(Collectors.toList());
            logger.info("second root detection processing " + rootImagesListPhase2.size() + " roots-start");
            rootImagesList.addAll(rootImagesListPhase2);


//            logger.info("root detection processing " + rootImagesList.size() + " plants");
//            RootDetection rootDetection = new RootDetection();
//            List<ClassifiedRootImage> classifiedRootImages = rootDetection.processRootImages(rootImagesList);
//
//            logger.info("root skeletonize processing " + classifiedRootImages.size() + " roots");
//            FindRootSkeleton rootSkeletonize = new FindRootSkeleton();
//            List<RootSkeleton> rootSkeletonList = rootSkeletonize.processRootImages(classifiedRootImages);
//
//            logger.info("determine drop processing " + rootSkeletonList.size() + " roots");
//            DetermineDropLocation determineDropLocation = new DetermineDropLocation();
//            List<DropLocation> dropLocationList = determineDropLocation.processRootSkeletonList(rootSkeletonList);

            List<RootSkeleton> rootSkeletonList = new ArrayList<>();
            List<DropLocation> dropLocationList = new ArrayList<>();

            List<RootModel> rootModelList = rootImagesList.stream().map(this::toRootModel).collect(Collectors.toList());
            List<PlantModel> plantModelList = rootSkeletonList.stream().map(this::toPlantModel).collect(Collectors.toList());
            List<DropModel> dropModelList = dropLocationList.stream().map(this::toDropModel).collect(Collectors.toList());

            return new ProcessResult("OK", dropModelList, plantModelList, rootModelList);

        } catch (Exception e) {
            logger.severe("Error processing image");
            return new ProcessResult("FOUT: " + e.getMessage());
        }
    }

    private List<RootImage> segment(RootImage rootImage) throws IOException {
        BlockStreamConfiguration blockStreamConfiguration = new BlockStreamConfiguration(5,2,6, new ComputeHistogram());
        ImageDescriptor hogDescriptor = ImageDescriptorFactory.newInstance().createHogDescriptor(blockStreamConfiguration);
        ImageClassifierConfiguration imageClassifierConfiguration = new ImageClassifierConfiguration(hogDescriptor, blockStreamConfiguration.getDescriptorSize());
        ImageClassifierSVM imageClassifier = new ImageClassifierSVM(imageClassifierConfiguration, Paths.get("trainingset", "roots", "model-root-svm.eg"));
        SlidingWindow slidingWindow = new SlidingWindow(rootImage.getBufferedImage(), imageClassifier, 20, 60, 10);
        return slidingWindow.process();
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
