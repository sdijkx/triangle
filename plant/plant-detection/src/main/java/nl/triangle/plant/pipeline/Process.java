package nl.triangle.plant.pipeline;

import nl.triangle.plant.pipeline.dto.ProcessResult;

import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * Created by steven on 27-06-16.
 */
public interface Process {
    ProcessResult process(Optional<BufferedImage> image);
}
