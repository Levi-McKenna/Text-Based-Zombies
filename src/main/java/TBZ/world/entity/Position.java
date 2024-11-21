package TBZ.world.entity;

import java.util.Objects;

public class Position {
    // fields
    private int x;
    private int y;

    public Position(int x, int y) {
        this.setX(x);
        this.setY(y);
    }
    // getters / setters
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // other methods

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return this.getX() == position.getX() && this.getY() == position.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
    // TODO - finish this method
    // public static boolean checkIfInBounds(Level level, int x, int y) {
    //     
    // }

    // operator overloads

    public Position plus(Position other) {
        return new Position(this.x + other.getX(), this.y + other.getY());
    }
}
