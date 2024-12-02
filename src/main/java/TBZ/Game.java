package TBZ;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// local imports 
import TBZ.world.World;
import TBZ.world.entity.Entity;
import TBZ.world.entity.Position;

/**
 * @author LMcKenna
 * @version 12.1.24
 *
 * This is an (almost) entirely in-terminal game made to replicate a similar
 * feel to Call of Duty Zombies (Mainly from Black Ops I || II || III). I'll
 * likely end up having to cut a lot from the game obviously, since I would like
 * to enjoy my Fall break a little. Here's the Github Repo:
 * https://github.com/Levi-McKenna/Text-Based-Zombies. I'll try and have the
 * .jar file in the releases tab. (Sorry if I forget to do it). I know this
 * might be a pain to grade. I'll be honest, I only glimpsed at the
 * requirements, and at the end of the day, I mainly made this project for
 * myself. 
 *
 * Oh and, there will be some bugs. This isn't thorouhgly tested, and
 * concurrency might be a problem in spots. My use of multi-threading was
 * subpar, and a usage of some form of timer would have been better
 * parallelization wise.
 */
public class Game {
    private World world;
    private String prompt;

    /**
     * Initializes this.world and sets the world directory
     */
    public Game() {
        this.world = new World("./levels/test");
    }    

    /**
     * @param prompt prompt to set 
     *
     * set this.prompt to prompt 
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * Runs the defined game. 
     */
    public void run() {
        this.world.spawnEntity(this.world.getPlayer());
        this.world.spawnEntity(this.world.getPlayer().getDSprite());

        // zombie spawning
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Random random = new Random();
                    int randIndex = random.nextInt(world.getLevel().getEnemySpawns().length);
                    Position randSpawn = world.getLevel().getEnemySpawns()[randIndex];
                    Zombie zombie = new Zombie(randSpawn);
                    world.spawnEntity(zombie);
                }
            }
        });


        while (true) {
            world.renderWorld();
            this.renderPrompt();
            // Zombie movement & damage
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    // Timer timer = new Timer();

                    // TimerTask task = new TimerTask() {
                    //     @Override
                    //     public void run() {
                    //         if (world.getPlayer().getHealth() != world.getPlayer().getMaxHealth()) {
                    //             world.healPlayer(25);
                    //             try {
                    //                 Thread.sleep(250);
                    //             } catch (InterruptedException e) {
                    //                 e.printStackTrace();
                    //             }
                    //         }
                    //     }
                    // };
                    for (int id = 0; id <= Collections.max(world.getEntities().keySet()); id++) {
                        if (world.getEntityByID(id) != null && world.getEntityByID(id) instanceof Zombie) {
                            Zombie zombie = (Zombie) world.getEntityByID(id);
                            zombie.move(zombie.findBestPath(world.getPlayer().getPosition()));
                            if (zombie.closeToTarget(world.getPlayer().getPosition())) {
                                world.damagePlayer(10);
                            }
                        }
                    }
                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        e.notify();
                    }
                }
            });
            // Do nothing for a bit to generate a stable framerate
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int id = 0; id <= Collections.max(world.getEntities().keySet()); id++) {
                if (world.getEntityByID(id) != null) {
                    Entity entity = world.getEntityByID(id);
                    this.world.setEntityPosition(entity);
                }
            }
        }
    }


    /**
     * Prints main screen
     *
     * (Didn't happen. Time constraints)
     */
    public void printMainMenu() {

    }

    /**
     * Render the players prompt (which is really just its UI. IDK why I didn't
     * call it UI.
     */
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
            + weapon1 + this.world.getPlayer().getWeapon(0) + " " + this.world.getPlayer().getCurrentAmmo(0) + "/" +  this.world.getPlayer().getMaxAmmo(0) + " | " 
            + weapon2 + (this.world.getPlayer().getWeapon(1) != null ? ("" + this.world.getPlayer().getWeapon(1) + " " + this.world.getPlayer().getCurrentAmmo(1) + "/" +  this.world.getPlayer().getMaxAmmo(1)) : "")
        );

        System.out.println(this.prompt);
    }
}
