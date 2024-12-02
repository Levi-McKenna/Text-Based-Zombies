package TBZ.world.entity;

import java.util.HashMap;

/**
 * @author LMcKenna
 * @version 12.1.24
 *
 * Abstract class to implement on entity types 
 */
public abstract class Entity {
    // fields
    private int id;
    private int health;
    private Position position;
    private char sprite;

    public int getID() {
        return this.id;
    }

    public int getHealth() {
        return this.health;
    }

    public Position getPosition() {
        return this.position;
    }

    public char getSprite() {
        return this.sprite;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setID(HashMap<Integer, Entity> entities) {
        this.id = generateID(entities);
    }

    public void setSprite(char sprite) {
        this.sprite = sprite;
    }

    /**
     * Generates an id one more than the greatest id within the map of entities 
     *
     * @param entities all current ids to entities 
     */
    public static int generateID(HashMap<Integer, Entity> entities) {
        // checks to see if it contains the first id, if so generate a new one
        int tmp = 0;
        for (int num : entities.keySet()) {
            if (num > tmp) tmp = num;
        }
        return tmp + 1;
    }

    /**
     * default move implementation 
     *
     * @param x x to move 
     * @param y y to move 
     */
    public void move(int x, int y) {
        int newX = this.getPosition().getX() + x;
        int newY = this.getPosition().getY() + y;

        this.setPosition(new Position(newX, newY));
    }

    /** 
     * default implementation for subtracting health 
     *
     * @param subtrahend amount to subtract 
     */
    public void substractHealth(int subtrahend) {
        if (this.getHealth() > subtrahend) {
            this.setHealth(this.getHealth() - subtrahend);
        } else this.setHealth(0);
    }
}
