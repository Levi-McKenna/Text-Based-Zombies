package TBZ;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// local imports 
import TBZ.world.World;
import TBZ.world.entity.Entity;
import TBZ.world.entity.Position;

public class Game {
    private World world;
    private HashMap<Integer, Entity> entities;
    private String prompt;

    public Game() {
        this.world = new World("./target/classes/TBZ/levels/test/");
        this.entities = new HashMap<>();
    }    

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void run() {
        this.spawnEntity(this.world.getPlayer());
        this.spawnEntity(this.world.getPlayer().getDSprite());

        // zombie spawning
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Random random = new Random();
                    int randIndex = random.nextInt(world.getLevel().getEnemySpawns().length);
                    Position randSpawn = world.getLevel().getEnemySpawns()[randIndex];
                    Zombie zombie = new Zombie(randSpawn);
                    spawnEntity(zombie);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        while (true) {
            world.renderWorld();
            this.renderPrompt();
            // Zombie movement
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    for (Entity entity : entities.values()) {
                        if (entity instanceof Zombie) {
                            Zombie zombie = (Zombie) entity;
                            zombie.move(zombie.findBestPath(world.getPlayer().getPosition()));
                        }
                    }
                    try {
                        Thread.sleep(750);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            // Do nothing for a bit to generate a stable framerate
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Entity entity : entities.values()) {
                this.world.setEntityPosition(entity);
            }
        }
    }

    /**
     * finds line to change and swaps the position with the entity 
     *
     * @param entity entity to spawn
     */
    public void spawnEntity(Entity entity) {
        // be sure to set the ID of the thang of course
        entity.setID(this.entities);
        this.world.setEntityPosition(entity);
        this.entities.put(entity.getID(), entity);
    }

    public void removeEntity(int id) {
        this.entities.get(id).setPosition(new Position(-1, -1));
        this.world.setEntityPosition(this.entities.get(id));
        // if this doesnt work then kill me
        this.entities.remove(id);
    }

    public void printMainMenu() {

    }

    public void renderPrompt() {
        String weapon1 = "1 - ";
        String weapon2 = "2 - ";
        switch (this.world.getPlayer().getWeaponIndex()) {
            case 0:
                weapon1 = "[1] - ";
                break;
            case 1:
                weapon2 = "[2] - ";
                break;
            default:
                weapon1 = "[1] - ";
                break;
        }
        this.setPrompt(
            "Health: " + this.world.getPlayer().getHealth() + "%" + " | " + this.world.getPlayer().getPoints() + "\t" + this.world.checkForInteractPrompt() + "\n"
            + weapon1 + this.world.getPlayer().getWeapon(0) + " | " + weapon2 + (this.world.getPlayer().getWeapon(1) != null ? this.world.getPlayer().getWeapon(1) : "")
        );

        System.out.println(this.prompt);
    }
}
