package TBZ.world.entity;

import java.util.Set;

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

    public void setID(Set<Entity> entities) {
        this.id = generateID(entities);
    }

    public void setSprite(char sprite) {
        this.sprite = sprite;
    }

    public static int generateID(Set<Entity> entities) {
        int maxId = 0;
        if (entities != null) {
            for (Entity entity : entities) {
                if (maxId == entity.getID() || maxId < entity.getID()) {
                    maxId = entity.getID();
                }
            }
        }
        return maxId++;
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
