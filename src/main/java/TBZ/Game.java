package TBZ;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// local imports 
import TBZ.world.World;
import TBZ.world.entity.Entity;
import TBZ.world.entity.Position;

public class Game {
    private World world;
    private String prompt;

    public Game() {
        this.world = new World("./target/classes/TBZ/levels/test/");
    }    

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

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
                        Thread.sleep(10000);
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
                    for (Entity entity : world.getEntities().values()) {
                        if (entity instanceof Zombie) {
                            Zombie zombie = (Zombie) entity;
                            zombie.move(zombie.findBestPath(world.getPlayer().getPosition()));
                            if (zombie.closeToTarget(world.getPlayer().getPosition())) {
                                world.damagePlayer(10);
                            }
                        }
                    }
                    try {
                        Thread.sleep(900);
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
            for (Entity entity : world.getEntities().values()) {
                this.world.setEntityPosition(entity);
            }
        }
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
