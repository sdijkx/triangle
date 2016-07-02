package nl.triangle.plant.classifier;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class ImageClassifierDummy implements ImageClassifier {

    private static final Random random = new Random();
    private final Path dest;

    public ImageClassifierDummy(Path dest) {
        this.dest = dest;
    }


    public boolean classifiy(Optional<BufferedImage> image) {
        try {
            if(random.nextInt() % 13 == 0) {
                Path filePath = dest.resolve(UUID.randomUUID().toString() + ".png");
                ImageIO.write(image.get(), "png", filePath.toFile());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
