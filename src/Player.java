import java.util.Set;
// local imports
import world.entity.Position;
import world.entity.Directions;
import world.entity.Entity;

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
