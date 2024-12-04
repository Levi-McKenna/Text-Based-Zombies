package TBZ.world;

/**
 * @author LMcKenna
 * @version 12.1.24
 * 
 * Enmerated type representing all weapons, their ammo, and damage
*/
public enum Weapons {
    PISTOL(8, 25),
    OLYMPIA(2, 75),
    M14(14, 50);

    private int ammo;
    public final int maxAmmo;
    public final int damage;

    private Weapons(int ammo, int damage) {
        this.ammo = ammo;
        this.maxAmmo = ammo;
        this.damage = damage;
    }

    /**
     * gets current ammo 
     *
     * @return current ammo of gun 
     */
    public int getAmmo() {
        return this.ammo;
    }

    /**
     * subtracts one ammo if the ammo is greater than 0
     */
    public void shoot() {
        if (this.ammo - 1 >= 0) this.ammo--; 
    }

    /**
     * reloads ammo to max ammo 
     */
    public void reload() {
        this.ammo = this.maxAmmo;
    }
}
