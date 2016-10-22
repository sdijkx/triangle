package nl.mh.test.robot.converter;

import nl.mh.test.robot.domain.Configuration;
import nl.mh.test.robot.domain.Coordinates;
import nl.mh.test.robot.domain.PhotoCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Marc on 5-7-2016.
 */
public class CoordinateConverter {
    private static Logger LOG = LoggerFactory.getLogger(CoordinateConverter.class);
    private Configuration config;

    public CoordinateConverter(Configuration config) {
        this.config = config;
    }

    public Coordinates convert(Coordinates coordinates, PhotoCoordinates photoCoordinates) {
        LOG.debug("X converter : {}, {}, {}, {}", coordinates.getX(), photoCoordinates.getPixelX(), getX(photoCoordinates), photoCoordinates);
        LOG.debug("Y converter : {}, {}, {}, {}", coordinates.getY(), photoCoordinates.getPixelY(), getY(photoCoordinates), photoCoordinates);

        return new Coordinates(
                coordinates.getX() + getX(photoCoordinates),
                coordinates.getY() + getY(photoCoordinates));
    }


    private int getX(PhotoCoordinates photoCoordinates) {
        return config.getPixelStepX(photoCoordinates.getPixelX());
    }


    private int getY(PhotoCoordinates photoCoordinates) {
        return config.getPixelStepY(photoCoordinates.getPixelY());
    }

}
