package nl.triangle.plant.pipeline.data;

import java.util.stream.Stream;

/**
 * Created by steven on 15-07-16.
 */
public class ProcessedPlantImage {
    private final PlantImage plantImage;
    private final Stream<DropLocation> dropLocationStream;

    public ProcessedPlantImage(PlantImage plantImage, Stream<DropLocation> dropLocationStream) {
        this.plantImage = plantImage;
        this.dropLocationStream = dropLocationStream;
    }

    public PlantImage getPlantImage() {
        return plantImage;
    }

    public Stream<DropLocation> getDropLocationStream() {
        return dropLocationStream;
    }


}
