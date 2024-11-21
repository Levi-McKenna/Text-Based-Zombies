package TBZ.world.entity;

import java.util.Collections;
import java.util.HashMap;

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

    public static int generateID(HashMap<Integer, Entity> entities) {
        // checks to see if it contains the first id, if so generate a new one
        if (entities.containsKey(1)) return Collections.max(entities.keySet())+1;
        return 1;
    }

    public void move(int x, int y) {
        int newX = this.getPosition().getX() + x;
        int newY = this.getPosition().getY() + y;

        this.setPosition(new Position(newX, newY));
    }

    public void substractHealth(int subtrahend) {
        if (this.getHealth() > subtrahend) {
            this.setHealth(this.getHealth() - subtrahend);
        } else this.setHealth(0);
    }
}
