package nl.mh.test.imagescanner;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Marc on 21-11-2015.
 */
public class FindFirst {
    public static final String NETWERK_FILE = "C:\\Users\\Marc\\Documents\\WS\\robot\\result\\netwerk\\encogexample.eg";
    public static final int BLOK_SIZE = 40;

    public static <R> void main(String... args) {
        new FindFirst().execute();
    }

    public void execute() {
        long start = System.currentTimeMillis();
        BasicNetwork network = loadNetwork();
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("cal1.jpg")) {
            BufferedImage image = javax.imageio.ImageIO.read(in);
            int step = 20;

            List<SubImage> list = IntStream.rangeClosed(0, (image.getHeight() - BLOK_SIZE) / step).boxed()
                    .flatMap(y -> IntStream.rangeClosed(0, (image.getWidth() - BLOK_SIZE) / step).boxed()
                            .map(x -> new SubImage(image, x * step, y * step)))
                    .collect(Collectors.toList());

            SubImage found = null;
            for (SubImage sub : list) {
                MLData result = network.compute(sub.getMLData());
                System.out.println("-->>> X: " + sub.getX() + ",  y:" + sub.getY() + "-->>>" + result.getData(0));
                if (result.getData(0) > 0.70) {
                    found = sub;
                    break;
                }
            }
            String kip = null;

            Graphics g = image.getGraphics();

            g.setColor (Color.RED);
            g.drawRect(found.getX(), found.getY(), BLOK_SIZE ,BLOK_SIZE);
            g.dispose();
            ImageIO.write(image, "jpg", new File("C:\\Users\\Marc\\Documents\\WS\\MH-Temp\\result\\result\\result_"+System.currentTimeMillis()+".jpg"));

            System.out.println("time: " +( (System.currentTimeMillis() -start)/ 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public BasicNetwork loadNetwork() {
        System.out.println("Loading network");
        return (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(NETWERK_FILE));
    }


    class SubImage {
        private int x;
        private int y;
        private BufferedImage parent;

        public SubImage(BufferedImage parent, int x, int y) {
            System.out.println("-->>> X: " + x + ",  y:" + y);
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        private BufferedImage getSubImage(BufferedImage parent, int x, int y) {
            try {
                BufferedImage sub = parent.getSubimage(x, y, BLOK_SIZE, BLOK_SIZE);
                ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                ColorConvertOp op = new ColorConvertOp(cs, null);
                return op.filter(sub, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public MLData getMLData() {
            BufferedImage sub = getSubImage(parent, x, y);
            BasicMLData input = new BasicMLData(BLOK_SIZE * BLOK_SIZE);
            IntStream.rangeClosed(0, BLOK_SIZE-1)
                    .forEach(i -> IntStream.rangeClosed(0, BLOK_SIZE-1)
                        .forEach(j -> input.add(i * BLOK_SIZE + j, (sub.getRGB(i, j) & 0xFF) / 256.0 )));
           return input;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
