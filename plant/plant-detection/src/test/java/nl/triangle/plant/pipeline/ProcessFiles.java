package nl.triangle.plant.pipeline;

import nl.triangle.plant.classifier.algorithms.imagedescriptor.image.ConvertToRGB;
import nl.triangle.plant.pipeline.dto.DropModel;
import nl.triangle.plant.pipeline.dto.PlantModel;
import nl.triangle.plant.pipeline.dto.ProcessResult;
import nl.triangle.plant.pipeline.dto.RootModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by steven on 14-05-16.
 */
public class ProcessFiles {

    public static void main(String[] args) throws IOException {

        ProcessImagePipeline pipeline = new ProcessImagePipeline();
        execute(args[0], (file) -> {
            try {
                BufferedImage image = ImageIO.read(file);
                BufferedImage rgb = convertToRGB(image);
                ProcessResult processResult = pipeline.process(Optional.of(rgb));
                writeResult(file, rgb, processResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static BufferedImage convertToRGB(BufferedImage image) {
        return new ConvertToRGB().convert(image);
    }

    private static void writeResult(File file, BufferedImage image, ProcessResult processResult) throws IOException {
        File output = new File("target/output/" + file.getName() + "-out.png");

        Graphics2D gd = image.createGraphics();

        gd.setColor(Color.RED);
        gd.setStroke(new BasicStroke(2));
        for (RootModel rootModel : processResult.getRootModelList()) {
            gd.drawRect(rootModel.getX(), rootModel.getY(), rootModel.getWidth(), rootModel.getHeight());
        }

        gd.setColor(Color.GRAY);
        gd.setStroke(new BasicStroke(1));
        for (DropModel dropLocation : processResult.getDropModelList()) {
            gd.drawOval(dropLocation.getX(), dropLocation.getY(), dropLocation.getWeight(), dropLocation.getWeight());
        }

        gd.setColor(Color.CYAN);
        gd.setStroke(new BasicStroke(2));
        for (PlantModel plantModel : processResult.getPlantModelList()) {
            gd.drawRect(plantModel.getX(), plantModel.getY(), plantModel.getWidth(), plantModel.getHeight());
        }
        gd.dispose();

        ImageIO.write(image, "png", output);
    }

    private static void execute(String pathName, Consumer<File> f) throws IOException {
        Path path = Paths.get(pathName);
        Files.list(path).filter(p -> p.toFile().isFile())
                .forEach(p -> f.accept(p.toFile()));
    }


}
