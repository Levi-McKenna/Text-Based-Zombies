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

/**
 * @author LMcKenna
 * @version 12.1.24 
 *
 * Represents a level and its modifications and all of its metadata 
 */
public class Level {
    public final List<String> level;
    public final Position bounds;
    private Position playerSpawn;
    private Position[] enemySpawns;
    private HashMap<Position, Interactable> interactables;

    public Level(String levelPath, String metaPath, int levelIndex) {
        this.level = Level.loadLevel(levelPath);
        this.bounds = new Position(this.level.get(0).length(), this.level.size());
        this.interactables = new HashMap<Position, Interactable>();
        this.parseMetadata(metaPath, levelIndex);
    }

    // getters/setters

    public List<String> getLevel() {
        return this.level;
    }

    public Position getPlayerSpawn() {
        return this.playerSpawn;
    }
    
    public HashMap<Position, Interactable> getInteractables() {
        return this.interactables;
    }

    public Position[] getEnemySpawns() {
        return this.enemySpawns;
    }

    public Position getBounds() {
        return this.bounds;
    }

    public void setInteractables(Position key, Interactable value) {
        this.interactables.put(key, value);
    }

    public void setPlayerSpawn(Position position) {
        this.playerSpawn = position;
    }

    public void setEnemySpawns(Position[] positions) {
        this.enemySpawns = positions;
    }

    public void buyInteractable(Position position) {
        this.interactables.get(position).buy();
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

    /**
     * parses json metadata for level 
     *
     * @param metaPath the path to json metadata file 
     * @param current levelIndex 
     */
    private void parseMetadata(String metaPath, int levelIndex) {
        try {
            File reader = new File(metaPath);
            System.out.println(metaPath);
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

            // set all doors for interactables map
            JSONArray doors = levelObj.getJSONArray("doors");
            this.setDoorInteractables(doors);

            // set all perks for interactables map
            JSONArray perks = levelObj.getJSONArray("perks");
            this.setPerkInteractables(perks);

            // set all wall buys
            JSONArray weapons = levelObj.getJSONArray("weapons");
            this.setWeaponInteractables(weapons);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * implementation for setting weapon interactables 
     *
     * @param weapons JSONArray of all weapons 
     */
    private void setWeaponInteractables(JSONArray weapons) {
        for (int i = 0; i < weapons.length(); i++) {
            JSONObject weapon = weapons.getJSONObject(i);
            // find key of type Position
            JSONObject position = weapon.getJSONObject("position");
            Position key = new Position(position.getInt("x"), position.getInt("y"));
            // intialize value of type Weapon
            int cost = weapon.getInt("cost");
            String weaponTypeS = weapon.getString("weapon");
            Weapons weaponType;
            switch (weaponTypeS) {
                case "PISTOL":
                    weaponType = Weapons.PISTOL;
                    break;
                case "M14":
                    weaponType = Weapons.M14;
                    break;
                case "OLYMPIA":
                    weaponType = Weapons.OLYMPIA;
                    break;
                default:
                    weaponType = Weapons.PISTOL;
                    break;
            }
            Weapon value = new Weapon(cost, weaponType);

            this.interactables.put(key, value);
        }
    }

    /**
     * implementation for setting all door interactables 
     *
     * @param doors JSONArray json of door data 
     */
    private void setDoorInteractables(JSONArray doors) {
            for (int i = 0; i < doors.length(); i++) {
                JSONObject door = doors.getJSONObject(i);
                // find key of type Position
                JSONObject position = door.getJSONObject("position");
                Position key = new Position(position.getInt("x"), position.getInt("y"));
                // initialize value of type Door
                int resultantLevelIndex = door.getInt("resultantLevel");
                int cost = door.getInt("cost");
                JSONObject resultantPositionObj = door.getJSONObject("resultantPosition");
                Position resultantPosition = new Position(resultantPositionObj.getInt("x"), resultantPositionObj.getInt("y"));
                boolean buyable = door.getBoolean("buyable");

                Door value = new Door(resultantPosition, resultantLevelIndex, cost, buyable);

                this.interactables.put(key, value);
            }
    }

    /**
     * implementation for setting all perk interactables 
     *
     * @param perks JSONArray json of perk data 
     */
    private void setPerkInteractables(JSONArray perks) {
            for (int i = 0; i < perks.length(); i++) {
                JSONObject perk = perks.getJSONObject(i);
                // find key of type position
                JSONObject position = perk.getJSONObject("position");
                Position key = new Position(position.getInt("x"), position.getInt("y"));
                // initialize value of type perk
                int cost = perk.getInt("cost");
                // DEV - Hard coded because only jug is implemented
                int healthMultiplier = 25;
                String perkType = perk.getString("perkType");
                
                Perk value = new Perk(healthMultiplier, cost, perkType);

                this.interactables.put(key, value);
            }
    }
}
