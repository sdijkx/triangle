package nl.triangle.plant.pipeline.processors;


import nl.triangle.plant.pipeline.data.ClassifiedRootImage;
import nl.triangle.plant.pipeline.data.RootImage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by steven on 14-05-15.
 */
public class RootDetection {
    public List<ClassifiedRootImage> processRootImages(List<RootImage> rootImagesList) {

        return rootImagesList.stream()
                .map(this::toClassifiedRootImage)
                .filter(img -> img != null)
                .collect(Collectors.toList());
    }

    private ClassifiedRootImage toClassifiedRootImage(RootImage rootImage) {
        if (classify(rootImage)) {
            return new ClassifiedRootImage(rootImage);
        }
        else {
            return null;
        }
    }

    private boolean classify(RootImage img) {
        return Math.random() > 0.5;
    }
}
