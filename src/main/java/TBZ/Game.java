package TBZ;

import java.util.HashSet;
import java.util.Set;
import java.awt.event.*;
import javax.swing.*;
// local imports 
import TBZ.world.World;
import TBZ.world.entity.Entity;
import TBZ.world.entity.Position;

public class Game extends JFrame implements KeyListener {
    private World world;
    private Set<Entity> entities;
    private Player player;

    public Game() {
        this.world = new World("./target/classes/TBZ/levels/test/");
        this.player = new Player(entities, this.world.getLevel().getPlayerSpawn());
        this.entities = new HashSet<>();
    }    

    public void run() {
        this.addKeyListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1900, 5);
        this.setVisible(true);

        this.spawnEntity(this.player);
        while (true) {
            world.renderWorld();
            for (Entity entity : entities) {
                this.world.setEntityPosition(entity);
            }
        }
    }

    // Key Input

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        switch (keycode) {
            case KeyEvent.VK_UP:
                player.move(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                player.move(0, 1);
                break;
            case KeyEvent.VK_RIGHT:
                player.move(1, 0);
                break;
            case KeyEvent.VK_LEFT:
                player.move(-1, 0);
                break;
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
        this.world.setEntityPosition(entity);
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entity.setPosition(new Position(-1, -1));
        this.world.setEntityPosition(entity);
        // if this doesnt work then kill me
        this.entities.remove(entity);
    }

    public void printMainMenu() {

    }
}
