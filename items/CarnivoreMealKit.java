package game.items;

import game.PortableItem;

/**
 * A class for CarnivoreMealKit item. CarnivoreMealKit can be bought in the
 * Vending Machine for 500 ecopoints. Players can feed Allosaurs with a
 * CarnivoreMealKit object. CarnivoreMealKit heals the Allosaur back to max hitpoints.
 *
 * @see game.PortableItem
 */
public class CarnivoreMealKit extends PortableItem {
    /**
     * Constructor for CarnivoreMealKit.
     */
    public CarnivoreMealKit() {
        super("Carnivore Meal Kit", 'C');
    }
}
