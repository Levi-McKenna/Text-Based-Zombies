package TBZ.world;

public class Perk extends Interactable{
    // Perks perkType;
    int healthMultiplier;
    String perkType;

    public Perk(int healthMultiplier, int cost, String perkType) {
        this.setHealthMultiplier(healthMultiplier);
        this.setCost(cost);
        this.setPerkType(perkType);
        this.setBuyable(true);
        this.setPrompt("Press E to buy " + perkType + " for " + cost + " points");
    }

    private void setHealthMultiplier(int healthMultiplier) {
        this.healthMultiplier = healthMultiplier;
    }

    private void setPerkType(String perkType) {
        this.perkType = perkType;
    }

    public int getHealthMultiplier() {
        return this.healthMultiplier;
    }

    public String getPerkType() {
        return this.perkType;
    }

    public void setPrompt() {
        if (this.isBuyable()) {
            this.setPrompt("Press E to buy " + this.getPerkType() + " for " + this.getCost() + " points");
        } else this.setPrompt("Press E to vend " + this.getPerkType());
    }

    @Override
    public void buy() {
        this.setCost(this.getCost() * 2);
        this.setBuyable(false);
        this.setPrompt();
    }

    public void vend() {
        this.setBuyable(true);
        this.setPrompt();
    }
}
