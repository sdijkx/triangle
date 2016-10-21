package nl.triangle.plant.pipeline;

import nl.triangle.plant.classifier.ImageClassifier;
import nl.triangle.plant.pipeline.classifiers.PlantClassifierFactory;
import nl.triangle.plant.pipeline.classifiers.RootClassifierFactory;
import nl.triangle.plant.pipeline.data.*;
import nl.triangle.plant.pipeline.dto.*;
import nl.triangle.plant.pipeline.processors.SlidingWindow;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by steven on 27-06-16.
 */
public class ProcessFactory {

    public ProcessFactory(ImageClassifier plantClassifier, ImageClassifier rootClassifier) {
        this.plantClassifier = plantClassifier;
        this.rootClassifier = rootClassifier;
    }

    public static ProcessFactory newInstance() throws IOException {
        return new ProcessFactory(new PlantClassifierFactory().create(), new RootClassifierFactory().create());
    }

    private final ImageClassifier plantClassifier;
    private final ImageClassifier rootClassifier;

    public Process create() {
        return (image) -> image.map(createProcessResult()).orElseThrow( () -> new IllegalStateException() );
    }

    private Function<BufferedImage, ProcessResult> createProcessResult() {

        return inputImage -> {
            SlidingWindow<PlantImage> plantImages = createSlidingWindowForPlants(inputImage);
            Stream<ProcessedPlantImage> processedPlantImageStream = plantImages.findImages().map(this::findRoots);
            return toProcessResult(processedPlantImageStream);
        };
    }

    private ProcessedPlantImage findRoots(PlantImage plantImage) {
        SlidingWindow<RootImage> rootImages = createSlidingWindowForRoots(plantImage);
        Stream<DropLocation> dropLocationStream = rootImages.findImages()
                .map(
                        detectRoots()
                                .andThen((rootImage) -> new RootSkeleton(
                                        new ClassifiedRootImage(
                                                new RootImage(plantImage.getBufferedImage(), plantImage.getX(), plantImage.getY(), plantImage)
                                        )))
                                .andThen(determineDroplocation())
                );
        return new ProcessedPlantImage(plantImage, dropLocationStream);
    }

    private SlidingWindow<RootImage> createSlidingWindowForRoots(PlantImage plantImage) {
        return new SlidingWindow<RootImage>(
                plantImage.getBufferedImage(),
                rootClassifier,
                5,
                (image2, x, y) -> new RootImage(image2, plantImage.getX() + x, plantImage.getY() + y, plantImage)
        );
    }

    private SlidingWindow<PlantImage> createSlidingWindowForPlants(BufferedImage image) {
        return new SlidingWindow<PlantImage>(
                image,
                plantClassifier,
                30,
                (image1, x, y) -> new PlantImage(image1, x, y)
        );
    }

    private ProcessResult toProcessResult(Stream<ProcessedPlantImage> processedPlantImageStream) {
        ProcessedPlantImageToModel processedPlantImageToModel = new ProcessedPlantImageToModel();
        List<PlantImageModel> plantImageModelList = processedPlantImageStream.map(processedPlantImageToModel::convert).collect(Collectors.toList());
        ProcessResult processResult = new ProcessResult("OK", plantImageModelList);
        return processResult;
    }

    private Function<RootSkeleton, DropLocation> determineDroplocation() {
        return rootSkeleton -> {
            return new DropLocation(rootSkeleton);
        };
    }

    private Function<ClassifiedRootImage, RootSkeleton> skeletonize() {
        return classifiedRootImage -> {
            return new RootSkeleton(classifiedRootImage);
        };
    }

    private Function<RootImage, ClassifiedRootImage> detectRoots() {
        return (rootImage) -> {
            return new ClassifiedRootImage(rootImage);
        };
    }


}
