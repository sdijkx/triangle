package nl.triangle.plant.pipeline.dto;

import nl.triangle.plant.pipeline.data.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by steven on 15-07-16.
 */
public class ProcessedPlantImageToModel {

    public PlantImageModel convert(ProcessedPlantImage processedPlantImage) {

        PlantImageModel plantImageModel = toPlantImageModel(processedPlantImage.getPlantImage());


        List<RootModel> rootModelList = processedPlantImage.getDropLocationStream().map(dropLocation -> {
            RootModel rootModel = toRootModel(dropLocation.getRootSkeleton().getClassifiedRootImage().getRootImage());
            RootSkeletonModel rootSkeletonModel = toRootSkeletonModel(dropLocation.getRootSkeleton());
            DropModel dropModel = toDropModel(dropLocation);
            rootSkeletonModel.setDropModel(dropModel);
            rootModel.setRootSkeletonModel(rootSkeletonModel);
            return rootModel;
        }).collect(Collectors.toList());

        plantImageModel.setRootModelList(rootModelList);
        return plantImageModel;
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

    private RootSkeletonModel toRootSkeletonModel(RootSkeleton rootSkeleton) {
        RootSkeletonModel rootSkeletonModel = new RootSkeletonModel();
        rootSkeletonModel.setBox(rootSkeleton.getTrendLine().getBox());
        rootSkeletonModel.setX(rootSkeleton.getX());
        rootSkeletonModel.setY(rootSkeleton.getY());
        return rootSkeletonModel;
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
