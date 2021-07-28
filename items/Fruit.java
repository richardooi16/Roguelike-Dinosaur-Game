package game.items;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import game.PortableItem;

/**
 * A class for Fruit item. Fruit will grow on Trees and Bushes. Players can feed
 * Stegosaurs and Brachiosaurs with a Fruit object. When Fruit is on the ground,
 * it will start rotting.
 *
 * @see game.PortableItem
 */
public class Fruit extends PortableItem {

    int freshness;

    /**
     * Constructor for Fruit.
     */
    public Fruit() {
        super("fruit", 'o');
        this.freshness = -1;
    }

    /**
     * Constructor for fruit.
     *
     * @param freshness the freshness of the fruit
     */
    public Fruit(int freshness) {
        super("fruit", 'o');
        this.freshness = freshness;
    }

    /**
     * Informs fruit of the passage of time when it is in an inventory.
     *
     * This method is called once per turn, if the Item is being carried.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        super.tick(currentLocation, actor);
    }

    /**
     * Informs fruit of the passage of time when it is on the ground.
     *
     * This method is called once per turn, if the item rests upon the ground.
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        rot();
        if (isRotten()) {
            currentLocation.removeItem(this);
        }
    }

    /**
     * Decrements the freshness of fruit by 1 every turn.
     */
    public void rot() {
        this.freshness -=1;
    }

    /**
     * Checks if the fruit is rotten. A fruit is rotten when its freshness is 0.
     *
     * @return true if freshness of fruit is 0, else return false.
     */
    public boolean isRotten() {
        return this.freshness==0;
    }
}
