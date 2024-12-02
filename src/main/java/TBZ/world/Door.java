package TBZ.world;

// local imports
import TBZ.world.entity.Position;

/**
 * @author LMcKenna
 * @version 12.1.24
 *
 * Represents door interactable 
 */
public class Door extends Interactable {
    public final Position resultantPosition;
    public final int resultantLevelIndex;

    public Door(Position resultantPosition, int resultantLevelIndex, int cost, boolean buyable) {
        this.resultantPosition = resultantPosition;
        this.resultantLevelIndex = resultantLevelIndex;
        this.setCost(cost);
        this.setBuyable(buyable);
        this.setPrompt();
    }

    public int getResultantLevelIndex() {
        return this.resultantLevelIndex;
    }

    public Position getResultantPosition() {
        return this.resultantPosition;
    }

    public void setPrompt() {
        if (this.isBuyable()) {
            this.setPrompt("Press E to buy for " + this.getCost() + " points");
        } else this.setPrompt("Press E to enter room");
    } 

    /**
     * sets the prompt and sets its buyable status
    */
    @Override
    public void buy() {
        this.setBuyable(false);
        this.setPrompt();
    }
}
