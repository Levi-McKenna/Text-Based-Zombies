package TBZ.world;

import java.util.List;
import java.io.File;
import java.util.HashMap;
// local imports 
import TBZ.world.entity.Entity;
import TBZ.world.entity.Position;

public class World {
    private Level level;
    private List<String> world;
    private String[] levelDirs;
    private Level[] levels;
    private HashMap<Integer, Position> idToPosition;
    private int levelIndex;
    private int healthAdder;

    public World(String levelDir) {
        this.healthAdder = 0;
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
        this.idToPosition = new HashMap<>();
    }

    // getters / setters

    public Level getLevel() {
        return this.level;
    }

    public int getHealthAdder() {
        return this.healthAdder;
    }

    public void setHealthAdder(int healthAdder) {
        this.healthAdder = healthAdder;
    }

    public void setLevel() {
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

    public void setLevelIndex(int index) {
        this.levelIndex = index;
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
            setWorldChar(this.idToPosition.get(entity.getID()), ' ');
            this.idToPosition.remove(entity.getID());
        }
    }

    private void setWorldChar(Position position, char sprite) {
        char[] lineToMutate = this.world.get(position.getY()).toCharArray();
        if (!isObstacle(lineToMutate[position.getX()]) || isPlayer(lineToMutate[position.getX()])) {
            lineToMutate[position.getX()] = sprite;
        }

        String newLine = "";
        for (char character : lineToMutate) {
            newLine = newLine + character;
        }
        world.set(position.getY(), newLine);

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

    // TODO - this needs to render all prompts and entities
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
            }
            return;
        }
    }

    private void interactPerk(Perk perk) {
        this.setHealthAdder(this.getHealthAdder() + perk.getHealthMultiplier());
    }

    private void interactDoor(Door door) {
        // move to next room
        this.updateLevels();
        this.setLevelIndex(door.getResultantLevelIndex());
        this.setLevel();
        this.setWorld();

        this.level.setPlayerSpawn(door.getResultantPosition());
    }


    // problems:
    // public void moveEntity(List<String> world, Position position) {
    //     int x = this.getPosition().getX();
    //     int y = this.getPosition().getY();

    //     // reset original position
    //     char[] prevLineToMutate = world.get(y).toCharArray();
    //     prevLineToMutate[x] = ' ';

    //     // i know this is shit but it works alright? fuck java
    //     String newLine = "";
    //     for (char character : prevLineToMutate) {
    //         newLine = newLine + character;
    //     }
    //     world.set(y, newLine);

    //     // set new position
    //     // TODO - Check if position is valid
    //     this.setPosition(position);
    //     x = this.getPosition().getX();
    //     y = this.getPosition().getY();

    //     lineToMutate[x] = this.getSprite();

    //     newLine = "";
    //     for (char character : prevLineToMutate) {
    //         newLine = newLine + character;
    //     }
    //     world.set(y, newLine);
    // }
}
