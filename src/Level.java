import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
// local imports
import world.entity.Position;

public class Level {
    public final List<String> level;
    public final Position bounds;
    // TODO - add all fields that correspond to metadat
    public final Position playerSpawn;
    public final Position[] enemySpawns;
    // public final Door[] doors;

    public Level(String path, Position playerSpawn) {
        this.level = Level.loadLevel(path);
        this.bounds = new Position(this.level.get(0).length(), this.level.size());
        this.playerSpawn = playerSpawn;
    }

    public List<String> getLevel() {
        return this.level;
    }

    public Position getPlayerSpawn() {
        return this.playerSpawn;
    }

    /**
     * Parses a level to an array of Strings 
     *
     * @param path the path of the level to load 
     * @return the complete level
     */
    private static List<String> loadLevel(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            // find array bound
            List<String> level = reader.lines().toList();
            // swap to mutable
            level = new ArrayList<>(level);
            reader.close();
            return level;

            // maybe? : 
            //// check if level is valid -  
            //// if any of the lengths are not equal, it's invalid
            //int tmpLength = level[1].length();
            //for (int i = 1; i < level.length; i++) {
            //    if (tmpLength != level[i].length()) {
            //        String[] errorReport = {"Error - Invalid level format"};
            //        return errorReport;
            //    } else tmpLength = level[i].length();
            //}

        } catch (IOException e) {
            e.printStackTrace();
        }

        // should only return when failed. (default return)
        List<String> errorReport = Arrays.asList("Error - Failed to resolve file to level");
        return errorReport;
    }

    // TODO - We also need some metadata for spawns, etc.
    private void parseMetadata() {
    }

}
