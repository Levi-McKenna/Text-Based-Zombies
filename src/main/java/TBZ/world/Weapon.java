package TBZ.world;

public class Weapon extends Interactable {
    Weapons weapon;

    public Weapon(int cost, Weapons weapon) {
        this.setBuyable(true);
        this.setCost(cost);
        this.setWeapon(weapon);
        this.setPrompt();
    }

    public Weapons getWeapon() {
        return this.weapon;
    }

    private void setWeapon(Weapons weapon) {
        this.weapon = weapon;
    }

    private void setPrompt() {
        if (this.isBuyable()) {
            this.setPrompt("Press E to buy " + this.getWeapon() + " for " + this.getCost() + " points");
        } else {
            this.setPrompt("Press E to equip " + this.getWeapon());
        }
    }

    public void equip() {
        this.setBuyable(true);
        this.setPrompt();
    }

    @Override
    public void buy() {
        this.setBuyable(false);
        this.setPrompt();
    }
}
