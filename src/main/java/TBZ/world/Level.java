package TBZ.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
// local imports
import TBZ.world.entity.Position;
import TBZ.world.Door;

public class Level {
    public final List<String> level;
    public final Position bounds;
    private Position playerSpawn;
    private Position[] enemySpawns;
    private HashMap<Position, Door> doors;

    public Level(String levelPath, String metaPath, int levelIndex) {
        this.level = Level.loadLevel(levelPath);
        this.bounds = new Position(this.level.get(0).length(), this.level.size());
        this.parseMetadata(metaPath, levelIndex);
    }

    // getters/setters

    public List<String> getLevel() {
        return this.level;
    }

    public Position getPlayerSpawn() {
        return this.playerSpawn;
    }

    private void setPlayerSpawn(Position position) {
        this.playerSpawn = position;
    }

    public void setEnemySpawns(Position[] positions) {
        this.enemySpawns = positions;
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
    private void parseMetadata(String metaPath, int levelIndex) {
        try {
            File reader = new File(metaPath);
            String content = new String(Files.readAllBytes(Paths.get(reader.toURI())));
            
            JSONObject obj = new JSONObject(content);
            JSONObject levelObj = obj.getJSONObject(Integer.toString(levelIndex));

            // set player spawn
            JSONObject playerSpawnObj = levelObj.getJSONObject("playerSpawn");
            Position playerSpawn = new Position(playerSpawnObj.getInt("x"), playerSpawnObj.getInt("y"));
            this.setPlayerSpawn(playerSpawn);

            // set enemy spawns
            JSONArray enemySpawns = levelObj.getJSONArray("spawns");
            this.enemySpawns = new Position[enemySpawns.length()];
            for (int i = 0; i < enemySpawns.length(); i++) {
                JSONObject enemySpawn = enemySpawns.getJSONObject(i);
                Position position = new Position(enemySpawn.getInt("x"), enemySpawn.getInt("y"));
                this.enemySpawns[i] = position;
            }

            // set all doors
            JSONArray doors = levelObj.getJSONArray("doors");
            this.doors = new HashMap<Position, Door>();
            for (int i = 0; i < doors.length(); i++) {
                JSONObject door = doors.getJSONObject(i);
                // find key of type Position
                JSONObject position = door.getJSONObject("position");
                Position key = new Position(position.getInt("x"), position.getInt("y"));
                // initialize value of type Door
                int resultantLevelIndex = door.getInt("resultantLevel");
                JSONObject resultantPositionObj = door.getJSONObject("resultantPosition");
                Position resultantPosition = new Position(resultantPositionObj.getInt("x"), resultantPositionObj.getInt("y"));
                Door value = new Door(resultantPosition, resultantLevelIndex);

                this.doors.put(key, value);
            }


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
    }
}
