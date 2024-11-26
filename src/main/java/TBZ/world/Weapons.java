package TBZ.world;

public enum Weapons {
    PISTOL(8, 25),
    OLYMPIA(2, 100),
    M14(14, 50);

    public final int ammo;
    public final int damage;

    private Weapons(int ammo, int damage) {
        this.ammo = ammo;
        this.damage = damage;
    }
}
