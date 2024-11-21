package TBZ.world;

// local imports
import TBZ.world.entity.Position;

public class Door extends Interactable {
    public final Position resultantPosition;
    public final int resultantLevelIndex;

    public Door(Position resultantPosition, int resultantLevelIndex, int cost) {
        this.resultantPosition = resultantPosition;
        this.resultantLevelIndex = resultantLevelIndex;
        this.setCost(cost);
        this.setBuyable(true);
        this.setPrompt("Press E to buy for " + cost + " points");
    }
}
