package nl.triangle.plant.pipeline.processors;


import nl.triangle.plant.pipeline.data.ClassifiedRootImage;
import nl.triangle.plant.pipeline.data.RootSkeleton;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by steven on 14-05-15.
 */
public class FindRootSkeleton {
    public List<RootSkeleton> processRootImages(List<ClassifiedRootImage> classifiedRootImages) {
        return classifiedRootImages.stream().filter(img -> img != null).map(this::skeletonize).collect(Collectors.toList());
    }
    private RootSkeleton skeletonize(ClassifiedRootImage img) {
        return new RootSkeleton(img);
    }
}
