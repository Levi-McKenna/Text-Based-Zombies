package TBZ;

// local imports
import TBZ.world.entity.Position;
import TBZ.world.Weapons;
import TBZ.world.entity.Directions;
import TBZ.world.entity.Entity;

public class Player extends Entity {
    // field
    private Directions direction;
    private DirectionSprite directionSprite;
    private int points;
    private Weapons[] weapons;
    private int weaponIndex;


    public Player(Position position) {
        this.setSprite('*');
        this.setPosition(position);
        this.setHealth(100);
        this.setPoints(0);
        this.weapons = new Weapons[2];
        this.setWeapon(0, Weapons.PISTOL);
        this.setWeaponIndex(0);
        this.directionSprite = new DirectionSprite('^', this
            .getPosition()
            .plus(new Position(0, -1)));
        this.setDirection(Directions.UP);
    }

    public Directions getDirection() {
        return this.direction;
    }

    public int getPoints() {
        return this.points;
    }

    public Entity getDSprite() {
        return directionSprite;
    }

    public Weapons[] getWeapons() {
        return this.weapons;
    }

    public Weapons getWeapon(int index) {
        return this.getWeapons()[index];
    }

    public int getWeaponIndex() {
        return this.weaponIndex;
    }

    public void setWeaponIndex(int index) {
        this.weaponIndex = index;
    }

    public void setWeapon(int index, Weapons weapon) {
        this.weapons[index] = weapon;
    }

    public void setCurrentWeapon(Weapons weapon) {
        this.setWeapon(this.getWeaponIndex(), weapon);
    }

    public void setPlayerPosition(Position position) {
        this.setPosition(position);
        this.setDirection(this.getDirection());
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addHealth(int health) {
        this.setHealth(this.getHealth() + health);
    }

    public void subtractHealth(int health) {
        this.setHealth(this.getHealth() - health);
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

    public void buy(int cost) {
        this.setPoints(this.getPoints() - cost);
    }

    public void addPoints(int points) {
        this.setPoints(this.getPoints() + points);
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
