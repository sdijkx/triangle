package nl.mh.test.robot.domain;

/**
 * Created by Marc on 15-6-2016.
 */
public class PhotoCoordinates  {
    private static int PHOTO_WIDTH_PX =  1000;
    private static int PHOTO_HEIGHT = 1000;
    private static double PIXEL_STEP = 1.4;
    private int pixelX;
    private int pixelY;

    public PhotoCoordinates( int pixelX, int pixelY) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
    }

    public int getPixelX() {
        return pixelX;
    }

    public int getPixelY() {
        return pixelY;
    }

    public PhotoCoordinates add (PhotoCoordinates other){
        return new PhotoCoordinates(this.getPixelX() + other.getPixelX(), this.getPixelY() + other.getPixelY());
    }

    @Override
    public String toString() {
        return "PhotoCoordinates{" +
                "pixelX=" + pixelX +
                ", pixelY=" + pixelY +
                '}';
    }
}
