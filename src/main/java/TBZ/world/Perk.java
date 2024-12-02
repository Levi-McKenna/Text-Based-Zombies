package TBZ.world;

/**
 * @author LMcKenna
 * @version 12.1.24 
 * 
 * Represents all perk interactables 
*/
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

    /**
     * Overloaded buy that multiplies cost by two and sets prompt 
     */
    @Override
    public void buy() {
        this.setCost(this.getCost() * 2);
        this.setBuyable(false);
        this.setPrompt();
    }

    /**
     * sets prompt and makes the perk buyable again 
     */
    public void vend() {
        this.setBuyable(true);
        this.setPrompt();
    }
}
