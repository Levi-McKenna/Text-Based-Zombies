package TBZ.world;

import java.util.List;
import java.util.Set;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.awt.event.*;
import javax.swing.*;
import TBZ.world.entity.Directions;
import TBZ.Player;
import TBZ.Zombie;
// local imports 
import TBZ.world.entity.Entity;
import TBZ.world.entity.Position;

public class World extends JFrame implements KeyListener {
    private Level level;
    private List<String> world;
    private String[] levelDirs;
    private Level[] levels;
    private HashMap<Integer, Entity> entities;
    private HashMap<Integer, Position> idToPosition;
    private Player player;
    private int levelIndex;

    /**
     * @param levelDir directory of world 
     *
     * Initializes instance 
     */
    public World(String levelDir) {
        // Key input setup
        this.addKeyListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1900, 5);
        this.setVisible(true);

        this.levelIndex = 0;

        // find all levels
        File dir = new File(levelDir);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            this.levelDirs = new String[files.length];
            this.levels = new Level[files.length-1];

            // add all directories to levelDirs
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].toPath().getFileName().toString().replaceFirst("[.][^.]+$", "");
                System.out.println(fileName);
                if (!fileName.equals("Metadata")) {
                    levelDirs[fileName.charAt(fileName.length()-1)-'1'] = files[i].toString();
                } else levelDirs[levelDirs.length-1] = files[i].toString();
            }
        } else {
            // for debugging
            System.out.println("Error - " + dir.getAbsolutePath() + " is not a directory");
            File pwd = new File(".");
            System.out.println(pwd.getAbsolutePath());
        }
        Level tmpLevel = new Level(levelDirs[levelIndex], levelDirs[levelDirs.length-1], levelIndex);
        this.level = tmpLevel;
        this.levels[levelIndex] = tmpLevel;
        // base world
        this.world = level.getLevel();
        this.entities = new HashMap<>();
        this.idToPosition = new HashMap<>();

        this.player = new Player(this.getLevel().getPlayerSpawn());
    }

    // getters / setters

    /** 
     * @return the current level of the world 
     *
     * returns the current level of the world 
     */
    public Level getLevel() {
        return this.level;
    }

    /** 
     * @return the player 
     *
     * returns the current instance of Player 
     */
    public Player getPlayer() {
        return this.player;
    }

    /** 
     * @return map of all ids to entities 
     *
     * returns entities 
     */
    public HashMap<Integer, Entity> getEntities() {
        return this.entities;
    }

    /** 
     * @param id id of entity
     * @return entity of id 
     *
     * returns entity with id 
     */
    public Entity getEntityByID(int id) {
        if (this.getEntities().get(id) != null) return this.getEntities().get(id);
        return null;
    }

    /**
     * sets current level to level of the current levelIndex 
     */
    public void setLevel() {
        // remove all zombies from level
            // this is weird but for lack of time: this prevents a for each loop
            // from panicking from a missing key in a reference (WHICH IS WACK. I
            // THOUGHT JAVA WOULD RETURN A CLONE OF THE VALUE. NOT A REFERENCE
            // TO THE OBJECT???? HELLO???? I MEAN I GUESS THAT WOULD BE WORSE
            // MEMORY MANAGEMENT BUT ISNT THAT THE WHOLE POINT OF HAVING GETTERS
            // AND SETTERS?? TO PREVENT ILLEGAL MUTATIONS OF PRIVATE VALUES??
            // WHY DO I GET A REFERENCE?? IS IT BECAUSE I'M NOT ASSIGNING IT. IT
            // IS NOT BECAUSE OF ASSIGNMENT. WHAT THE FLIP JAVA. WHAT IS THIS
            // SHIT. WHATT?????????? YOU ONLY RETURN A REFERENCE TO OBJECT.
            // PROBABLY AN IMMUTABLE ONE??? I HOPE)
        for (int id = 0; id <= Collections.max(this.getEntities().keySet()); id++) {
            if (this.getEntityByID(id) != null && this.getEntityByID(id) instanceof Zombie) {
                this.removeEntity(id);
            }
        }
        if (levels[levelIndex] != null) {
            this.level = levels[levelIndex];
            return;
        }

        Level level = new Level(levelDirs[levelIndex], levelDirs[levelDirs.length-1], levelIndex);
        this.levels[levelIndex] = level;
        this.level = level;
    }

    /** 
     * sets world to current level 
     */
    private void setWorld() {
        this.world = this.level.getLevel();
    }

    /**
     * sets the level in the field levels to the updated modified level 
     */
    private void updateLevels() {
        this.levels[levelIndex] = this.level;
    }

    /**
     * @param index index to set 
     *
     * sets index of level 
     */
    public void setLevelIndex(int index) {
        this.levelIndex = index;
    }

    /**
     * finds line to change and swaps the position with the entity 
     *
     * @param entity entity to spawn
     */
    public void spawnEntity(Entity entity) {
        // be sure to set the ID of the thang of course
        entity.setID(this.entities);
        this.setEntityPosition(entity);
        this.entities.put(entity.getID(), entity);
    }

    /** 
     * @param id id of entity to remove 
     *
     * removes entity of id 
     */
    public void removeEntity(int id) {
        this.entities.get(id).setPosition(new Position(-1, -1));
        this.setEntityPosition(this.entities.get(id));
        // if this doesnt work then kill me
        this.entities.remove(id);
    }

    /** 
     * @param entity 
     *
     * sets all entities to their current position in the world 
     */
    public void setEntityPosition(Entity entity) {
        if (idToPosition == null || idToPosition.get(entity.getID()) == null) {
            setWorldChar(entity.getPosition(), entity.getSprite());
            this.idToPosition.put(entity.getID(), entity.getPosition());
            return;
        }

        setWorldChar(idToPosition.get(entity.getID()), ' ');
        if (!entity.getPosition().equals(new Position(-1, -1))) {
            this.idToPosition.replace(entity.getID(), entity.getPosition());
            setWorldChar(entity.getPosition(), entity.getSprite());
        // remove entity from world
        } else {
            this.idToPosition.remove(entity.getID());
        }
    }

    /** 
     * @param position position of character to replace 
     * @param sprite the character to replace with 
     *
     * sets character at position with the sprite 
     */
    private void setWorldChar(Position position, char sprite) {
        if (position.getY() >= this.world.size() || position.getY() < 0) return;
        char[] lineToMutate = this.world.get(position.getY()).toCharArray();
        if (!isObstacle(lineToMutate[position.getX()]) || isPlayer(lineToMutate[position.getX()])) {
            lineToMutate[position.getX()] = sprite;
        } else if (isZombie(lineToMutate[position.getX()]) && !isPlayer(sprite)) {
            lineToMutate[position.getX()] = sprite;
        }

        String newLine = "";
        for (char character : lineToMutate) {
            newLine = newLine + character;
        }
        world.set(position.getY(), newLine);

    }

    // Key Input

    /**
     * @param e key event 
     *
     * Handles key presses 
     */
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        Position tmpSpawn = this.getLevel().getPlayerSpawn();

        Position newPos = new Position(0, 0);
        switch (keycode) {
            case KeyEvent.VK_W:
                if (this.player.getDirection() == Directions.UP) {
                    newPos = new Position(0, -1);
                } else {
                    player.setDirection(Directions.UP);
                }
                break;
            case KeyEvent.VK_S:
                if (this.player.getDirection() == Directions.DOWN) {
                    newPos = new Position(0, 1);
                } else {
                    player.setDirection(Directions.DOWN);
                }
                break;
            case KeyEvent.VK_D:
                if (this.player.getDirection() == Directions.RIGHT) {
                    newPos = new Position(1, 0);
                } else {
                    player.setDirection(Directions.RIGHT);
                }
                break;
            case KeyEvent.VK_A:
                if (this.player.getDirection() == Directions.LEFT) {
                    newPos = new Position(-1, 0);
                } else {
                    player.setDirection(Directions.LEFT);
                }
                break;
            case KeyEvent.VK_E:
                // interact if there is something to interact with
                this.interact(this.player.getDSprite().getPosition());

                // this is shit but it checks if the player spawn has changed.
                // if it has then it's a new level and we can move the player
                if (!tmpSpawn.equals(this.getLevel().getPlayerSpawn())) {
                    player.setPlayerPosition(this.getLevel().getPlayerSpawn());
                }
                break;
            case KeyEvent.VK_1:
                // switch to weapon 1
                this.player.setWeaponIndex(0);
                break;
            case KeyEvent.VK_2:
                this.player.setWeaponIndex(1);
                break;
            case KeyEvent.VK_SPACE:
                if (getPlayer().getCurrentAmmo(getPlayer().getWeaponIndex()) > 0 && getPlayer().getCurrentWeapon() != null) {
                    this.player.shoot();
                    int id = this.findProjectileTarget(this.player.getDirection(), this.getPlayer().getPosition());
                    if (id != 0) this.player.addPoints(25);
                    this.damageEntity(this.player.getCurrentWeaponDamage(), id);
                }
                break;
            case KeyEvent.VK_R:
                this.player.reload();
                break;
        }
        // check to make sure player will not collide
        if (!this.willCollide(this.player.getPosition().plus(newPos))) {
            player.move(newPos.getX(), newPos.getY());
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    /**
     * @return the prompt if there is one for interacting 
     *
     * returns an interactable prompt if there is one in front of the player 
     */
    public String checkForInteractPrompt() {
        if (this.isInteractable(this.player.getDSprite().getPosition())) {
            return this.interactPrompt(this.player.getDSprite().getPosition());
        }
        return "";
    }

    /**
     * checks for if the character is an object that should not be walked over
     *
     * @param character char to check
     * @return true if it is an object, false otherwise 
     */
    private boolean isObstacle(char character) {
        if (
            character != ' ' &&
            character != '^' &&
            character != '>' &&
            character != '<'
        ) return true;
        return false;
    }

    /** 
     * checks to see if character is of the player sprite 
     *
     * @param character char to check
     * @return true if it is player, false otherwise 
     */
    public boolean isPlayer(char character) {
        if (character == '*') return true;
        return false;
    }

    /** 
     * checks to see if the character is a zombie 
     *
     * @param character char to check 
     * @return true if it is a zombie, false otherwise 
     */
    public boolean isZombie(char character) {
        if (character == '@') return true;
        return false;
    }

    /** 
     * clears the screen and prints the level with all entities and sprites 
     */
    public void renderWorld() {
        // clear screen
        System.out.print("\033[H\033[2J");
        for (String line : this.world) {
            System.out.println(line);
        }
    }

    /**
     * checks to see if a position will collide with an obstacle 
     *
     * @param position to check 
     * @return true if it will collide, false otherwise 
     */
    public boolean willCollide(Position position) {
        char[] line = this.world.get(position.getY()).toCharArray();
        char character = line[position.getX()];

        return isObstacle(character);
    }

    /** 
     * checks to see if position is interactable 
     *
     * @param position position to check 
     * @return true if the position is interactable, false otherwise 
     */
    public boolean isInteractable(Position position) {
        if (this.level.getInteractables().get(position) != null) return true;
        return false;
    }

    /**
     * returns the prompt of interactable at specific position 
     *
     * @param position position of interactable 
     * @return returns the interact prompt 
     */
    public String interactPrompt(Position position) {
        if (isInteractable(position)) {
            return this.level.getInteractables().get(position).getPrompt();
        }
        return "";
    }

    /**
     * interacts with specific position 
     *
     * @param position position to interact with 
     */
    public void interact(Position position) {
        Interactable interactable = this.level.getInteractables().get(position);

        if (isInteractable(position) && interactable.isBuyable()) {
            // default behavior for all interactables
            if (getPlayer().getPoints() >= interactable.getCost()) {
                this.level.buyInteractable(position);
                player.subtractPoints(interactable.getCost());
            }
        } else {
            if (interactable instanceof Door) interactDoor((Door) interactable);
            else if (interactable instanceof Perk) {
                Perk perk = (Perk) interactable;
                interactPerk(perk);
                perk.vend();
                this.level.setInteractables(position, perk);
            } else if (interactable instanceof Weapon) {
                Weapon weapon = (Weapon) interactable;
                interactWeapon(weapon);
                weapon.equip();
                this.level.setInteractables(position, weapon);
            }
            return;
        }
    }

    /** 
     * implementation for weapon interacts 
     *
     * @param weapon the weapon interactable 
     */
    private void interactWeapon(Weapon weapon) {
        for (int i = 0; i < this.player.getWeapons().length; i++) {
            if (this.player.getWeapons()[i] == null) {
                this.player.setWeapon(i, weapon.getWeapon());
                return;
            }
        }
        this.player.setCurrentWeapon(weapon.getWeapon());
    }

    /** 
     * implementation for perk interactables 
     *
     * @param perk perk to interact with 
     */
    private void interactPerk(Perk perk) {
        this.player.addMaxHealth(perk.getHealthMultiplier());
    }

    /** 
     * implementation for door interactable 
     *
     * @param door to interact with 
     */
    private void interactDoor(Door door) {
        // move to next room
        this.updateLevels();
        this.setLevelIndex(door.getResultantLevelIndex());
        this.setLevel();
        this.setWorld();

        this.level.setPlayerSpawn(door.getResultantPosition());
    }

    /** 
     * heal player by so much health 
     *
     * @param health the health to heal 
     */
    public void healPlayer(int health) {
        this.player.addHealth(health);
    }

    /** 
     * damage player by so much 
     *
     * @param damage amount of health to damage player 
     */
    public void damagePlayer(int damage) {
        this.player.subtractHealth(damage);
        if (this.getPlayer().getHealth() <= 0) {
            this.removeEntity(1);
            this.removeEntity(2);
            System.exit(0);
        }
    }

    /** 
     * damage entity with specific id 
     *
     * @param damage amount to damage 
     * @param id id of entity to damage 
     */
    public void damageEntity(int damage, int id) {
        if (getEntityByID(id) == null) return;
        this.entities.get(id).substractHealth(damage);
        if (this.entities.get(id).getHealth() <= 0) {
            this.removeEntity(id);
            this.player.addPoints(50);
        }
    } 

    /** 
     * find hit scan target 
     *
     * @param direction direction the projectile is moving 
     * @param position starting position of projectile 
     */
    public int findProjectileTarget(Directions direction, Position position) {
        Position addend = new Position(0, 0);
        Position finalPosition = new Position(0, 0);
        switch (direction) {
            case RIGHT:
                addend = new Position(1, 0);
                finalPosition = new Position(this.getLevel().getBounds().getX(), position.getY());
                break;
            case LEFT:
                addend = new Position(-1, 0);
                finalPosition = new Position(0, position.getY());
                break;
            case UP:
                addend = new Position(0, -1);
                finalPosition = new Position(position.getX(), 0);
                break;
            case DOWN:
                addend = new Position(0, 1);
                finalPosition = new Position(position.getX(), this.getLevel().getBounds().getY());
                break;
        }
        for (Position projPos = new Position(0, 0); !finalPosition.equals(projPos.plus(position)); projPos = projPos.plus(addend)) {
            if (projPos.equals(new Position(0, 0))) continue;
            for (int id = 3; id <= Collections.max(this.getEntities().keySet()); id++) {
                if (getEntityByID(id) != null && getEntityByID(id).getPosition().equals(position.plus(projPos))) {
                    return id;
                }
            }
        }
        return 0;
    }
}
