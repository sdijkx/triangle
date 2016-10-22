package nl.mh.test.robot.domain;

/**
 * Created by Marc on 16-6-2016.
 */
public class Coordinates {
    public static Coordinates HOME = new Coordinates(0, 0);
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public Coordinates add(Coordinates other) {
        return new Coordinates(getX() + other.getX(), getY() + other.getY());
    }

    public Coordinates add(int x, int y) {
        return new Coordinates(getX() + x, getY() + y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (x != that.x) return false;
        return y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
