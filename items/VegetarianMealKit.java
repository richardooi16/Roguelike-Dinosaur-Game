package game.items;

import game.PortableItem;

/**
 * A class for VegetatianMealKit item. VegetatianMealKit can be bought in the
 * Vending Machine for 100 ecopoints. Players can feed Stegosaurs and Brachiosaurs
 * with a VegetatianMealKit object. VegetatianMealKit heals the Stegosaurs and
 * Brachiosaurs back to max hitpoints.
 *
 * @see game.PortableItem
 */
public class VegetarianMealKit extends PortableItem {
    /**
     * Constructor for VegetarianMealKit.
     */
    public VegetarianMealKit() {
        super("Vegetarian Meal Kit", 'V');
    }
}
