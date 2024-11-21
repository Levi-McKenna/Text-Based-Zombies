package TBZ.world.entity; 


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

    public boolean equals(Position position) {
        if (this.getX() == position.getX()) return true;
        else if (this.getY() == position.getY()) return true;

        return false;
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
