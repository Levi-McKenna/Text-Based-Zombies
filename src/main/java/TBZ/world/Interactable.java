package TBZ.world;

/**
 * @author LMcKenna
 * @version 12.1.24
 *
 * Abstract class for default implementations of all interactable items 
 */
public abstract class Interactable {
    private boolean buyable;
    private int cost;
    private String prompt;

    public int getCost() {
        return this.cost;
    }

    public boolean isBuyable() {
        return this.buyable;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public void setBuyable(boolean buyable) {
        this.buyable = buyable;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * deafault buy implementation 
     */
    public void buy() {
        setBuyable(false);
    }
}
