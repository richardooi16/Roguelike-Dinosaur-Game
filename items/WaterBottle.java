package game.items;

import game.PortableItem;

/**
 * A class for WaterBottle item. WaterBottle can be bought in the
 * Vending Machine for 100 ecopoints. Players can feed Dinosaurs
 * with a WaterBottle object. WaterBottle restores the Dinosaurs
 * back to max water level.
 *
 * @see game.PortableItem
 */
public class WaterBottle extends PortableItem {
    /**
     * Constructor for VegetarianMealKit.
     */
    public WaterBottle() {
        super("Water Bottle", '%');
    }
}