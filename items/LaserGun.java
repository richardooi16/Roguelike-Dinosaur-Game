package game.items;

import edu.monash.fit2099.engine.WeaponItem;

/**
 * A LaserGun weapon that can be used by the player to attack.
 */
public class LaserGun extends WeaponItem {
    /**
     * Constructor.
     */
    public LaserGun() {
        super("Laser Gun", '>', 50, "zaps");
    }
}
