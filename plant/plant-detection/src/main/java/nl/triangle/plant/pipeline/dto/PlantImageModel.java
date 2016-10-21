package nl.triangle.plant.pipeline.dto;

import java.util.List;

/**
 * Created by steven on 09-06-16.
 */
public class PlantImageModel {

    private int x;
    private int y;
    private int width;
    private int height;

    private List<RootModel> rootModelList;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<RootModel> getRootModelList() {
        return rootModelList;
    }

    public void setRootModelList(List<RootModel> rootModelList) {
        this.rootModelList = rootModelList;
    }
}
