package nl.triangle.plant.pipeline;

import com.google.gson.Gson;
import nl.triangle.plant.pipeline.dto.ProcessImageRequest;
import nl.triangle.plant.pipeline.dto.ProcessResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Logger;

import static spark.Spark.*;

/**
 * Created by steven on 14-05-15.
 */
public class RestApplication {

    private static final Logger logger = Logger.getLogger(RestApplication.class.getName());

    public static void main(String[] args) {

        ProcessImagePipeline pipeline = new ProcessImagePipeline();
        Gson gson = new Gson();
        port(9000);

        post("/process", (req, res) -> {
            ProcessImageRequest processImageRequest = gson.fromJson(req.body(), ProcessImageRequest.class);
            BufferedImage image = ImageIO.read(new URL(processImageRequest.getImageUrl()));
            return pipeline.process(Optional.of(image));
        }, gson::toJson);

        post("/process/binary", (req, res) -> {
            byte[] imageData = req.bodyAsBytes();
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
            return pipeline.process(Optional.of(image));
        }, gson::toJson);


        exception(Exception.class, (e, req, res) -> {
            res.status(500);
            res.body(gson.toJson(new ProcessResult("ERROR")));
        });

    }
}
