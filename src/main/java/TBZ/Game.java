package TBZ;

import java.util.HashMap;
import java.awt.event.*;
import javax.swing.*;
// local imports 
import TBZ.world.World;
import TBZ.world.entity.Entity;
import TBZ.world.entity.Position;
import TBZ.world.entity.Directions;

public class Game extends JFrame implements KeyListener {
    private World world;
    private HashMap<Integer, Entity> entities;
    private Player player;
    private String prompt;

    public Game() {
        this.world = new World("./target/classes/TBZ/levels/test/");
        this.player = new Player(this.world.getLevel().getPlayerSpawn());
        this.entities = new HashMap<>();
    }    

    public void run() {
        this.addKeyListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1900, 5);
        this.setVisible(true);

        this.spawnEntity(this.player);
        this.spawnEntity(this.player.getDSprite());
        while (true) {
            checkForInteractPrompt();
            world.renderWorld();
            this.renderPrompt();
            // Do nothing for a bit to generate a stable framerate
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Entity entity : entities.values()) {
                this.world.setEntityPosition(entity);
            }
        }
    }

    // Key Input

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        Position newPos = new Position(0, 0);
        switch (keycode) {
            case KeyEvent.VK_UP:
                if (this.player.getDirection() == Directions.UP) {
                    newPos = new Position(0, -1);
                } else {
                    player.setDirection(Directions.UP);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (this.player.getDirection() == Directions.DOWN) {
                    newPos = new Position(0, 1);
                } else {
                    player.setDirection(Directions.DOWN);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (this.player.getDirection() == Directions.RIGHT) {
                    newPos = new Position(1, 0);
                } else {
                    player.setDirection(Directions.RIGHT);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (this.player.getDirection() == Directions.LEFT) {
                    newPos = new Position(-1, 0);
                } else {
                    player.setDirection(Directions.LEFT);
                }
                break;
        }
        // check to make sure player will not collide
        if (!this.world.willCollide(this.player.getPosition().plus(newPos))) {
            player.move(newPos.getX(), newPos.getY());
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

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

    public String checkForInteractPrompt() {
        if (this.world.isInteractable(this.player.getDSprite().getPosition())) {
            return this.world.interactPrompt(this.player.getDSprite().getPosition());
        }
        return "";
    }

    public void renderPrompt() {
        // TODO - Add player inventory
        this.prompt = this.player.getHealth() + " " + checkForInteractPrompt() + "\n";

        System.out.println(this.prompt);
    }
}
