package nl.triangle.plant.pipeline.data;

/**
 * Created by steven on 14-05-15.
 */
public class DropLocation {

    private int x;
    private int y;
    private int weight;
    private final RootSkeleton rootSkeleton;

    public DropLocation(RootSkeleton rootSkeleton) {
        this.rootSkeleton = rootSkeleton;
        x = (int) (3 + rootSkeleton.getX() + rootSkeleton.getTrendLine().getBox()[1]);
        y = (int) (60 + rootSkeleton.getY() + rootSkeleton.getTrendLine().getBox()[3]);
        weight = 30;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWeight() {
        return weight;
    }

    public RootSkeleton getRootSkeleton() {
        return rootSkeleton;
    }
}
