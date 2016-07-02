package nl.triangle.plant.classifier.algorithms.coordinatesets;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by steven on 10-04-16.
 */
public class CoordinateSet {
    private Collection<Coordinate> coordinates = new ArrayList<>();
    public void add(Coordinate coordinate) {
        coordinates.add(coordinate);
    }

    public Box getBoundingBox() {
        Coordinate minx = coordinates.stream().min((coordinate1, coordinate2) -> Double.compare(coordinate1.getX(),coordinate2.getX())).get();
        Coordinate miny = coordinates.stream().min((coordinate1, coordinate2) -> Double.compare(coordinate1.getY(),coordinate2.getY())).get();
        Coordinate maxx = coordinates.stream().max((coordinate1, coordinate2) -> Double.compare(coordinate1.getX(), coordinate2.getX())).get();
        Coordinate maxy = coordinates.stream().max((coordinate1, coordinate2) -> Double.compare(coordinate1.getY(), coordinate2.getY())).get();
        return new Box(new Coordinate(minx.getX(),miny.getY()), new Coordinate(maxx.getX(),maxy.getY()));
    }

    public double getAspectRatio() {
        Box box = getBoundingBox();
        return (double)box.getWidth()/(double)box.getHeight();
    }

    public Integer size() {
        return coordinates.size();
    }

    public Collection<Coordinate> getCoordinates() {
        return coordinates;
    }
}
