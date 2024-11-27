package TBZ;

import TBZ.world.entity.Entity;
import TBZ.world.entity.Position;

public class Zombie extends Entity {
    public Zombie(Position spawn) {
        this.setPosition(spawn);
        this.setHealth(100);
        this.setSprite('@');
    }

    public Position findBestPath(Position tVec) {
        Position curPos = this.getPosition();
        int dX = tVec.getX() - curPos.getX();
        int dY = tVec.getY() - curPos.getY();

        int x = 0;
        int y = 0;
        int absDX = ((int) Math.sqrt(Math.pow(dX, 2)));
        int absDY = ((int) Math.sqrt(Math.pow(dY, 2)));

        if (absDX > 1) x = dX / absDX;
        if (absDY > 1) y = dY / absDY;

        if (absDX == 1 && absDY == 1) y = dY / absDY;

        return new Position(x, y);
    }

    public void move(Position vector) {
        this.setPosition(this.getPosition().plus(vector));
    }
}
