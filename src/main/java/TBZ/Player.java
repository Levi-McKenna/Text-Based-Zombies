package TBZ;

// local imports
import TBZ.world.entity.Position;
import TBZ.world.Weapons;
import TBZ.world.entity.Directions;
import TBZ.world.entity.Entity;

/**
 * @author LMcKenna
 * @version 12.1.24
 *
 * This class is meant to represent the player character and all associated
 * attributes 
 */
public class Player extends Entity {
    // fields
    private Directions direction;
    private DirectionSprite directionSprite;
    private int points;
    private Weapons[] weapons;
    private int weaponIndex;
    private int maxHealth;


    /**
     * @param position position for player to originate from.
     *
     * Creates insance of player character from position 
     */
    public Player(Position position) {
        this.setSprite('*');
        this.setPosition(position);
        this.setHealth(100);
        this.setMaxHealth(100);
        this.setPoints(0);
        this.weapons = new Weapons[2];
        this.setWeapon(0, Weapons.PISTOL);
        this.setWeaponIndex(0);
        this.directionSprite = new DirectionSprite('^', this
            .getPosition()
            .plus(new Position(0, -1)));
        this.setDirection(Directions.UP);
    }

    /**
     * @return returns the players current direction 
     */
    public Directions getDirection() {
        return this.direction;
    }

    /**
     * @return return the players current points 
     */
    public int getPoints() {
        return this.points;
    }

    /** 
     * @return the direction sprite entity 
     *
     * gets the direction sprite entity 
     */
    public Entity getDSprite() {
        return directionSprite;
    }

    /**
     * @return the players weapon inventory
     *
     * returns players weapon inventory
     */
    public Weapons[] getWeapons() {
        return this.weapons;
    }

    /**
     * @param index index to return
     * @return returns weapon at index of inventory
     *
     * returns weapon at specified index
     */
    public Weapons getWeapon(int index) {
        return this.getWeapons()[index];
    }

    /**
     * @return the player's current weapon index 
     *
     * returns the player's current weapon index 
     */
    public int getWeaponIndex() {
        return this.weaponIndex;
    }

    /**
     * @return the weapon player has currently selected 
     *
     * returns the weapon at player's weapon index 
     */
    public Weapons getCurrentWeapon() {
        return getWeapons()[getWeaponIndex()];
    }

    /**
     * @return the damage of the currently selected weapon 
     *
     * returns the currently selected weapon's damage
     */
    public int getCurrentWeaponDamage() {
        return this.getWeapons()[getWeaponIndex()].damage;
    }

    /**
     * @return the maximum player health
     *
     * returns the maximum player health 
     */
    public int getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * @param index the index of the player's weapon inventory to check for ammo 
     * @return the current ammo of specific weapon
     *
     * returns ammo of specified weapon 
     */
    public int getCurrentAmmo(int index) {
        if (index < this.weapons.length) {
            return this.weapons[index].getAmmo(); 
        }
        return 0;
    }

    /**
     * @param index of player's weapon inventory
     * @return max ammo of selected weapon
     *
     * returns the max ammo of the weapon at index of weapon inventory. returns
     * 0 by default.
     */
    public int getMaxAmmo(int index) {
        if (index < this.weapons.length) {
            return this.weapons[index].maxAmmo; 
        }
        return 0;
    }

    /**
     * @param maxHealth the max to set the player's health.
     * 
     * sets max health
     */
    private void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * @param index the index to set 
     *
     * sets the player's weapon index 
     */
    public void setWeaponIndex(int index) {
        this.weaponIndex = index;
    }

    /**
     * @param index the index of player weapon inventory
     * @param weapon the weapon to set 
     *
     * sets the selected index to the selected weapon in player's weapon
     * inventory
     */
    public void setWeapon(int index, Weapons weapon) {
        this.weapons[index] = weapon;
    }

    /**
     * @param weapon the weapon to set 
     *
     * sets the weapon at the player's current weapon weapon 
     */
    public void setCurrentWeapon(Weapons weapon) {
        this.setWeapon(this.getWeaponIndex(), weapon);
    }

    /**
     * @param position position to set 
     * 
     * sets the player's position 
     */
    public void setPlayerPosition(Position position) {
        this.setPosition(position);
        this.setDirection(this.getDirection());
    }

    /**
     * @param points points to set 
     *
     * sets the player's points 
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @param health health to add 
     *
     * add health to player's current health 
     */
    public void addHealth(int health) {
        this.setHealth(this.getHealth() + health);
    }

    /**
     * @param health extra health to add to the max health of player 
     *
     * adds health to player's max health 
     */
    public void addMaxHealth(int health) {
        this.setHealth(this.getMaxHealth() + health);
        this.setMaxHealth(this.getMaxHealth() + health);
    }

    /**
     * @param health health to subtract 
     *
     * subtracts current health from player 
     */
    public void subtractHealth(int health) {
        if (this.getHealth() - health <= 0) {
            this.setHealth(0);
        } else {
            this.setHealth(this.getHealth() - health);
        }
    }

    /** 
     * @param points points to subtract 
     *
     * subtracts points from player's current healht 
     */
    public void subtractPoints(int points) {
        if (this.getPoints() - points <= 0) {
            this.setPoints(0);
        } else this.setPoints(this.getPoints() - points);
    }

    /** 
     * @param direction the direction to set 
     *
     * sets player's direction 
     */
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

    /**
     * @param cost cost of interactable 
     *
     * subtracts the cost of interactable from player's points 
     */
    public void buy(int cost) {
        this.subtractPoints(cost);
    }

    /**
     * @param points points to add 
     *
     * the amount of points to add 
     */
    public void addPoints(int points) {
        this.setPoints(this.getPoints() + points);
    }

    /**
     * @param x x direction to move 
     * @param y y direction to move 
     *
     * moves the player AND its direction sprite 
     */
    public void move(int x, int y) {
        Position move = new Position(x, y);
        // set player position & it's direction sprite
        this.setPosition(this.getPosition().plus(move));

        this.directionSprite.setPosition(this.directionSprite.getPosition().plus(move)); 
        
    }

    /**
     * subtracts one ammo from curret weapon 
     */
    public void shoot() {
        this.weapons[this.getWeaponIndex()].shoot();
    }

    /**
     * reloads ammo of current weapon 
     */
    public void reload() {
        this.weapons[this.getWeaponIndex()].reload();
    }
}

/**
 * @author LMcKenna
 * @version 12.1.24
 *
 * The direction entity for player 
 */
class DirectionSprite extends Entity {
    /**
     * @param sprite the initial sprite
     * @param position initial position
     *
     * Initializes direction entity for player
     */
    public DirectionSprite(char sprite, Position position) {
        this.setSprite(sprite);
        this.setPosition(position);
    }
}
