package nl.mh.test.imagescanner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

/**
 * Created by Marc on 13-11-2015.
 */
public class ImageScanner {

    private static String BASE_PATH = "C:\\Users\\Marc\\Documents\\WS\\datasets\\";
//    private static String SET_NAME = "calibration-stip-preface";
    private static String SET_NAME = "calibration-stip";
//    public static final int BLOK_SIZE = 40;
    public static final int BLOK_SIZE = 28;
//    public static final int STEP= 5;
    public static final int STEP= 1;

    private static String PROCECC_DIR = "\\imagestosplit\\process";

    public static void main(String... args) {
        try {
            Files.walk(Paths.get(BASE_PATH  +SET_NAME  + PROCECC_DIR))
                    .filter(Files::isRegularFile)
                    .forEach(ImageScanner::processFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(Path file) {
        try (InputStream in = Files.newInputStream(file)) {
            BufferedImage image = javax.imageio.ImageIO.read(in);
            int step = STEP;
            int bloksize = BLOK_SIZE;
            IntStream.range(0, image.getHeight() / step)
                    .forEach(i -> IntStream.range(0, image.getWidth() / step)
                            .filter(j -> i * step + bloksize <= image.getHeight() && j * step + bloksize <= image.getWidth())
                            .forEach(j -> writeSub(image, i * step, j * step, bloksize,file.getFileName().toString())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeSub(BufferedImage image, int i, int j, int bloksize, String name) {
        try {
            BufferedImage sub = image.getSubimage(j, i, bloksize, bloksize);
            new File("result/images").mkdirs();
            File outputfile = new File(BASE_PATH + SET_NAME + "/pretraining/"+name+"_" + j + "_" + i + "_.jpg");
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            sub = op.filter(sub, null);
            ImageIO.write(sub, "jpg", outputfile);
            System.out.println(outputfile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
