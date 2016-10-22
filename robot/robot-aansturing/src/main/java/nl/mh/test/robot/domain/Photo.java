package nl.mh.test.robot.domain;

import java.io.File;

/**
 * Created by Marc on 4-7-2016.
 */
public class Photo {
    private final File imageFile;
    private final Coordinates createdCoordinates;

    public Photo(File imageFile, Coordinates createdCoordinates) {
        this.imageFile = imageFile;
        this.createdCoordinates = createdCoordinates;
    }

    public File getImageFile() {
        return imageFile;
    }

    public Coordinates getCreatedCoordinates() {
        return createdCoordinates;
    }
}
