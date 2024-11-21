package TBZ;

// local imports
import TBZ.world.entity.Position;
import TBZ.world.entity.Directions;
import TBZ.world.entity.Entity;

public class Player extends Entity {
    // field
    private Directions direction;
    private DirectionSprite directionSprite;


    public Player(Position position) {
        this.setSprite('*');
        this.setPosition(position);
        this.setHealth(100);
        this.directionSprite = new DirectionSprite('^', this
            .getPosition()
            .plus(new Position(0, -1)));
        this.setDirection(Directions.UP);
    }

    public Directions getDirection() {
        return this.direction;
    }

    public Entity getDSprite() {
        return directionSprite;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
        switch (direction) {
            case UP:
                this.directionSprite.setSprite('^');
                this.directionSprite.setPosition(new Position(0, -1).plus(this.getPosition()));
                break;
            case DOWN:
                this.directionSprite.setSprite(' ');
                this.directionSprite.setPosition(new Position(0, 1).plus(this.getPosition()));
                break;
            case RIGHT:
                this.directionSprite.setSprite('>');
                this.directionSprite.setPosition(new Position(1, 0).plus(this.getPosition()));
                break;
            case LEFT:
                this.directionSprite.setSprite('<');
                this.directionSprite.setPosition(new Position(-1, 0).plus(this.getPosition()));
                break;
        }
    }

    public void move(int x, int y) {
        Position move = new Position(x, y);
        // set player position & it's direction sprite
        this.setPosition(this.getPosition().plus(move));

        this.directionSprite.setPosition(this.directionSprite.getPosition().plus(move)); 
        
    }
}

class DirectionSprite extends Entity {
    public DirectionSprite(char sprite, Position position) {
        this.setSprite(sprite);
        this.setPosition(position);
    }
}
