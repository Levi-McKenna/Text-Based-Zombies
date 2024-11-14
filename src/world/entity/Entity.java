package world.entity;

import java.util.Set;

public abstract class Entity {
    // fields
    private int id;
    private Position position;
    private char sprite;

    public int getID() {
        return this.id;
    }

    public Position getPosition() {
        return this.position;
    }

    public char getSprite() {
        return this.sprite;
    }

    // default implementation
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
}
