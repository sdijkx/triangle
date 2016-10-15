package nl.triangle.plant.pipeline;

import nl.triangle.plant.classifier.ImageClassifier;
import nl.triangle.plant.classifier.ImageClassifierSVM;
import nl.triangle.plant.pipeline.data.*;
import nl.triangle.plant.pipeline.dto.*;
import nl.triangle.plant.pipeline.processors.SlidingWindow;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by steven on 27-06-16.
 */
public class ProcessFactory {

    public static ProcessFactory newInstance() {
        return new ProcessFactory();
    }

    public Process create() {
        return (image) -> image.map(createProcessResult()).orElseThrow( () -> new IllegalStateException() );
    }

    private Function<BufferedImage, ProcessResult> createProcessResult() {

        return image -> {
            Stream<ProcessedPlantImage> processedPlantImageStream = findPlants(image).map(plantImage -> {
                Stream<DropLocation> dropLocationStream = findRoots().apply(plantImage).map(detectRoots().andThen(skeletonize()).andThen(determineDroplocation()));
                return new ProcessedPlantImage(plantImage, dropLocationStream);
            });

            ProcessedPlantImageToModel processedPlantImageToModel = new ProcessedPlantImageToModel();
            List<PlantImageModel> plantImageModelList = processedPlantImageStream.map(processedPlantImageToModel::convert).collect(Collectors.toList());

            ProcessResult processResult = new ProcessResult("OK", plantImageModelList);
            return processResult;
        };
    }

    private Stream<PlantImage> findPlants(BufferedImage image) {
        try {
            ImageClassifier imageClassifier = new PlantClassifierFactory().create();
            int imageWidth = imageClassifier.getConfiguration().getWidth();
            int imageHeight = imageClassifier.getConfiguration().getHeight();
            SlidingWindow<PlantImage> slidingWindow = new SlidingWindow<>(
                    image,
                    imageClassifier, imageWidth, imageHeight, 30,
                    (image1, x, y) -> new PlantImage(image1, x, y)
            );
            return slidingWindow.process();
        } catch (IOException e) {
            return null;
        }
    }

    private Function<PlantImage, Stream<DropLocation>> interm() {
        return plantImage ->
                        findRoots().apply(plantImage)
                        .map(detectRoots())
                        .map(skeletonize())
                        .map(determineDroplocation());
    }


    private Function<PlantImage, Stream<RootImage>> findRoots() {
        return plantImage -> {
                try {
                    ImageClassifierSVM imageClassifier = new RootClassifierFactory().create();
                    int imageWidth = imageClassifier.getConfiguration().getWidth();
                    int imageHeight = imageClassifier.getConfiguration().getHeight();
                    SlidingWindow<RootImage> slidingWindow = new SlidingWindow<>(
                            plantImage.getBufferedImage(),
                            imageClassifier, imageWidth , imageHeight, 10,
                            (image, x, y) -> {
                                return new RootImage(image, plantImage.getX() + x, plantImage.getY() + y, plantImage);
                            }
                    );
                    return slidingWindow.process();
                } catch (IOException e) {
                    return null;
                }
          };
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

    private PlantImageModel toPlantImageModel(PlantImage plantImage) {
        PlantImageModel plantImageModel = new PlantImageModel();
        plantImageModel.setX(plantImage.getX());
        plantImageModel.setY(plantImage.getY());
        plantImageModel.setWidth(plantImage.getBufferedImage().getWidth());
        plantImageModel.setHeight(plantImage.getBufferedImage().getHeight());
        return plantImageModel;
    }


}
