package TBZ;

import TBZ.world.entity.Entity;
import TBZ.world.entity.Position;

public class Zombie extends Entity {
    private int damage;

    public Zombie(Position spawn) {
        this.setPosition(spawn);
        this.setHealth(100);
        this.setSprite('@');
        this.setDamage(25);
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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

    public boolean closeToTarget(Position tVec) {
        Position path = this.findBestPath(tVec);
        if (path.getX() == 0 && path.getY() == 0) return true;
        return false;
    }

    public void move(Position vector) {
        this.setPosition(this.getPosition().plus(vector));
    }
}
