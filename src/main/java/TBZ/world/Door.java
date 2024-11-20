package TBZ.world;

// local imports
import TBZ.world.entity.Position;

public class Door {
    public final Position resultantPosition;
    public final int resultantLevelIndex;

    public Door(Position resultantPosition, int resultantLevelIndex) {
        this.resultantPosition = resultantPosition;
        this.resultantLevelIndex = resultantLevelIndex;
    }
}
