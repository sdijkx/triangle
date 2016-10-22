package nl.mh.test.robot.imagerecognition;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.stream.IntStream;

/**
 * Created by Marc on 22-6-2016.
 */
public class ImageData {
    private int x;
    private int y;
    private BufferedImage parent;
    private int bloksize;

    public ImageData(BufferedImage parent, int x, int y, int bloksize) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.bloksize = bloksize;
    }

    private BufferedImage getSubImage(BufferedImage parent, int x, int y) {
        try {
            BufferedImage sub = parent.getSubimage(x, y, bloksize, bloksize);
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            return op.filter(sub, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getParent() {
        return parent;
    }

    public int getBloksize() {
        return bloksize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
