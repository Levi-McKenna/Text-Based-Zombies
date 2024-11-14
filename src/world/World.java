package world;

import java.util.List;
import java.io.File;
import java.util.HashMap;
// local imports 
import world.entity.Entity;
import world.entity.Position;

public class World {
    private Level level;
    private List<String> world;
    private String[] levelDirs;
    private HashMap<Integer, Position> idToPosition;
    private int levelIndex;

    public World(String levelDir) {
        this.levelIndex = 0;

        // find all levels
        File dir = new File(levelDir);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            this.levelDirs = new String[files.length];

            // add all directories to levelDirs
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i]);
                levelDirs[i] = files[i].toString();
            }
        } else {
            System.out.println("Error - Not a directory");
        }
        this.level = new Level(levelDirs[levelIndex]);
        this.world = level.getLevel();
        this.idToPosition = new HashMap<>();
    }

    // getters / setters

    public Level getLevel() {
        return this.level;
    }

    public void setEntityPosition(Entity entity) {
        if (idToPosition == null || idToPosition.get(entity.getID()) == null) {
            setWorldChar(entity.getPosition(), entity.getSprite());
            this.idToPosition.put(entity.getID(), entity.getPosition());
            return;
        }

        setWorldChar(idToPosition.get(entity.getID()), ' ');
        this.idToPosition.replace(entity.getID(), entity.getPosition());
        setWorldChar(entity.getPosition(), entity.getSprite());
    }

    private void setWorldChar(Position position, char sprite) {
        char[] lineToMutate = this.world.get(position.getY()).toCharArray();
        lineToMutate[position.getX()] = sprite;

        String newLine = "";
        for (char character : lineToMutate) {
            newLine = newLine + character;
        }
        world.set(position.getY(), newLine);

    }

    // TODO - this needs to render all prompts and entities
    public void renderWorld() {
        // clear screen
        System.out.print("\033[H\033[2J");
        for (String line : this.world) {
            System.out.println(line);
        }
        // Do nothing for a bit to generate a stable framerate
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
