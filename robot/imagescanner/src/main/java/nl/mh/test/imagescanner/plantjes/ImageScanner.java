package nl.mh.test.imagescanner.plantjes;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.InputStream;
import java.util.stream.IntStream;

/**
 * Created by Marc on 14-1-2016.
 */
public class ImageScanner {
    public static void main(String... args) {

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("plantjes/Train_knipset.jpg")) {
            BufferedImage image = javax.imageio.ImageIO.read(in);
            int step = 1;
            int bloksize = 20;
            IntStream.range(0, image.getHeight() / step)
                    .forEach(i -> IntStream.range(0, image.getWidth() / step)
                            .filter(j -> i * step + bloksize <= image.getHeight() && j * step + bloksize <= image.getWidth())
                            .forEach(j -> writeSub(image, i * step, j * step, bloksize)));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void writeSub(BufferedImage image, int i, int j, int bloksize) {
        try {
            BufferedImage sub = image.getSubimage(j, i, bloksize, bloksize);
            new File("result/plantjes/images").mkdirs();
            File outputfile = new File("result/plantjes/images/sub_2_" + j + "_" + i + ".jpg");
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
