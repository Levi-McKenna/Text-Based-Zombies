import java.util.Set;

import javax.swing.JTextField;

// local imports
import world.entity.Position;
import world.entity.Entity;

public class Player extends Entity {
    // field

    public Player(Set<Entity> entities, Position position) {
        this.setID(entities);
        this.setSprite('*');
        this.setPosition(position);
    }
}
