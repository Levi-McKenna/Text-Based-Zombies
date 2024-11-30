package TBZ.world;

import java.util.List;
import java.io.File;
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
    private boolean clearEntities;

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
                levelDirs[i] = files[i].toString();
            }
        } else {
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

        this.clearEntities = false;
        this.player = new Player(this.getLevel().getPlayerSpawn());
    }

    // getters / setters

    public Level getLevel() {
        return this.level;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HashMap<Integer, Entity> getEntities() {
        return this.entities;
    }

    public boolean getClearEntities() {
        return this.clearEntities;
    }

    public void setLevel() {
        // remove all zombies from level
        for (Entity entity : this.getEntities().values()) {
            if (entity instanceof Zombie) {
                this.removeEntity(entity.getID());
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

    private void setWorld() {
        this.world = this.level.getLevel();
    }

    private void updateLevels() {
        this.levels[levelIndex] = this.level;
    }

    public void setClearEntities(boolean bool) {
        this.clearEntities = bool;
    }

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

    public void removeEntity(int id) {
        this.entities.get(id).setPosition(new Position(-1, -1));
        this.setEntityPosition(this.entities.get(id));
        // if this doesnt work then kill me
        this.entities.remove(id);
    }

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

    private void setWorldChar(Position position, char sprite) {
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

    public String checkForInteractPrompt() {
        if (this.isInteractable(this.player.getDSprite().getPosition())) {
            return this.interactPrompt(this.player.getDSprite().getPosition());
        }
        return "";
    }

    private boolean isObstacle(char character) {
        if (
            character != ' ' &&
            character != '^' &&
            character != '>' &&
            character != '<'
        ) return true;
        return false;
    }

    public boolean isPlayer(char character) {
        if (character == '*') return true;
        return false;
    }

    public boolean isZombie(char character) {
        if (character == '@') return true;
        return false;
    }

    public void renderWorld() {
        // clear screen
        System.out.print("\033[H\033[2J");
        for (String line : this.world) {
            System.out.println(line);
        }
    }

    public boolean willCollide(Position position) {
        char[] line = this.world.get(position.getY()).toCharArray();
        char character = line[position.getX()];

        return isObstacle(character);
    }

    public boolean isInteractable(Position position) {
        if (this.level.getInteractables().get(position) != null) return true;
        return false;
    }

    public String interactPrompt(Position position) {
        if (isInteractable(position)) {
            return this.level.getInteractables().get(position).getPrompt();
        }
        return "";
    }

    public void interact(Position position) {
        Interactable interactable = this.level.getInteractables().get(position);

        if (isInteractable(position) && interactable.isBuyable()) {
            // default behavior for all interactables
            this.level.buyInteractable(position);
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

    private void interactWeapon(Weapon weapon) {
        for (int i = 0; i < this.player.getWeapons().length; i++) {
            if (this.player.getWeapons()[i] == null) {
                this.player.setWeapon(i, weapon.getWeapon());
                return;
            }
        }
        this.player.setCurrentWeapon(weapon.getWeapon());
    }

    private void interactPerk(Perk perk) {
        this.player.addMaxHealth(perk.getHealthMultiplier());
    }

    private void interactDoor(Door door) {
        // move to next room
        this.updateLevels();
        this.setLevelIndex(door.getResultantLevelIndex());
        this.setLevel();
        this.setWorld();

        this.level.setPlayerSpawn(door.getResultantPosition());
    }

    public void healPlayer(int health) {
        this.player.addHealth(health);
    }

    public void damagePlayer(int damage) {
        this.player.subtractHealth(damage);
    }
}
