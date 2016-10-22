package nl.mh.test.robot.imagerecognition.loopstrategy;

import nl.mh.test.robot.imagerecognition.ImageData;

import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * Created by Marc on 23-6-2016.
 */
public class FromCenterLoopStrategy implements LoopStrategy {
    enum Stage {UP, LEFT, BOTTOM, RIGHT}

    private final BufferedImage baseImage;
    private final double[][] convertedImage;
    private final int bloksize;
    private final int step;

    public FromCenterLoopStrategy(BufferedImage baseImage, int bloksize, int step) {
        this.baseImage = baseImage;
        this.bloksize = bloksize;
        this.step = step;
        convertedImage = initializeOn();
    }

    private double[][] initializeOn() {


        return new double[0][];
    }


    @Override
    public Iterator<ImageData> iterator() {
        return new Loop(baseImage, bloksize, step);
    }


    class Loop implements Iterator<ImageData> {
        private final BufferedImage baseImage;

        private final int bloksize;
        private final int step;
        private int x;
        private int y;
        private int deep;
        private int round = 0;
        private int xEnd;
        private int yEnd;
        private ImageData next;
        private Stage stage;


        public Loop(BufferedImage baseImage, int bloksize, int step) {
            this.baseImage = baseImage;
            this.bloksize = bloksize;
            this.step = step;
            deep = getStart();
            xEnd = getXEnd();
            yEnd = getYEnd();
            stage = Stage.BOTTOM;
            next = createSubImage(xEnd, yEnd);
        }


        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public ImageData next() {
            ImageData toReturn = next;
            next = getNext();
            return toReturn;
        }

        private ImageData getNext() {
            ImageData image = null;
            switch (stage) {
                case UP:
                    if (x + step > xEnd + round) {
                        stage = Stage.RIGHT;
                        image = getNext();
                    } else image = createSubImage(x + step, y);
                    break;
                case RIGHT:
                    if (y + step > yEnd + round) {
                        stage = Stage.BOTTOM;
                        image = getNext();
                    } else image = createSubImage(x, y + step);
                    break;
                case BOTTOM:
                    if (x - step < deep - round) {
                        stage = Stage.LEFT;
                        image = getNext();
                    } else image = createSubImage(x - step, y);
                    break;
                case LEFT:
                    if (y - step < deep - round) {
                        stage = Stage.UP;
                        if (round - step > deep) {
                            image = null;
                        } else {
                            round = round + step;
                            image = createSubImage(deep -round , deep -round );
                        }
                    } else image = createSubImage(x, y-step);
                    break;
            }


            return image;
        }


        private ImageData createSubImage(int x, int y) {
            this.x = x;
            this.y = y;

            if (x < 0 || y < 0) return null;
            if (x + bloksize > baseImage.getWidth()) return null;
            if (y + bloksize > baseImage.getHeight()) return null;
            return new ImageData(baseImage, x, y, bloksize);
        }

        private int getStart() {
            return (Math.min(baseImage.getWidth(), baseImage.getHeight()) - bloksize) / 2;
        }

        private int getXEnd() {
            return baseImage.getWidth() - deep - bloksize;
        }

        private int getYEnd() {
            return baseImage.getHeight() - deep - bloksize;
        }
    }

}
