package nl.triangle.plant.pipeline.processors;


import nl.triangle.plant.pipeline.data.DropLocation;
import nl.triangle.plant.pipeline.data.RootSkeleton;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by steven on 14-05-15.
 */
public class DetermineDropLocation {
    public List<DropLocation> processRootSkeletonList(List<RootSkeleton> rootSkeletonList) {
        return rootSkeletonList.stream().map(this::findLocation).collect(Collectors.toList());
    }
    private DropLocation findLocation(RootSkeleton rootSkeleton) {
        return new DropLocation(rootSkeleton);
    }
}
