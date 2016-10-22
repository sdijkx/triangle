package nl.mh.test.robot.imagerecognition.converters;

import nl.mh.test.robot.imagerecognition.ImageData;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.stream.IntStream;

/**
 * Created by Marc on 27-6-2016.
 */
public class GrayScaleConverter implements Converter {

    @Override
    public MLData convert(ImageData subImage) {
        BasicMLData input = new BasicMLData(subImage.getBloksize() * subImage.getBloksize());
        IntStream.rangeClosed(0, subImage.getBloksize()-1)
                .forEach(i -> IntStream.rangeClosed(0, subImage.getBloksize()-1)
                        .forEach(j -> input.add(i * subImage.getBloksize() + j, (subImage.getParent().getRGB( subImage.getX() + i, subImage.getY() + j) & 0xFF) / 256.0 )));
        return input;
    }
}
