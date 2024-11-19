package TBZ;

import java.util.Set;
// local imports
import TBZ.world.entity.Position;
import TBZ.world.entity.Directions;
import TBZ.world.entity.Entity;

public class Player extends Entity {
    // field
    private Directions direction;


    public Player(Set<Entity> entities, Position position) {
        this.setID(entities);
        this.setSprite('*');
        this.setPosition(position);
        this.setHealth(100);
        this.setDirection(Directions.UP);
    }

    public Directions getDirection() {
        return this.direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }
}
