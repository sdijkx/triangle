package nl.mh.test.robot.imagerecognition.converters;

import nl.mh.test.robot.imagerecognition.ImageData;
import org.encog.ml.data.MLData;

/**
 * Created by Marc on 28-6-2016.
 */
public interface Converter {
    public MLData convert(ImageData subImage);
}
