package TBZ.world.entity;

import java.util.Objects;

/**
 * @author LMcKenna
 * @version 12.1.24 
 *
 * X, Y Vector for entity and other positions 
 */
public class Position {
    // fields
    private int x;
    private int y;

    /**
     * @param x x value to set 
     * @param y y value to set 
     *
     * initializes an instance of Position 
     */
    public Position(int x, int y) {
        this.setX(x);
        this.setY(y);
    }
    // getters / setters
    /** 
     * @return x value of instance 
     *
     * returns x value of instance 
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return y value of instance 
     *
     * returns y value of position 
     */
    public int getY() {
        return this.y;
    }

    /**
     * @param x value to set 
     *
     * sets x value 
     */
    public void setX(int x) {
        this.x = x;
    }

    /** 
     * @param y value to set 
     *
     * sets y value 
     */
    public void setY(int y) {
        this.y = y;
    }

    // other methods

    /**
     * @param obj object to compare
     * @return true if equal, false if not
     *
     * compares object to position to chcek if they are equal 
     */
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

    /**
     * @param other other position to add
     * @return sum of positions 
     *
     * adds two positions 
     */
    public Position plus(Position other) {
        return new Position(this.x + other.getX(), this.y + other.getY());
    }
}
