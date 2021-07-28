package game.items;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Item;

/**
 * A Waypoint item to allow players to travel to the other side of the map.
 *
 * @see edu.monash.fit2099.engine.GameMap
 */
public class Waypoint extends Item {
    /***
     * Constructor.
     *  @param name the name of this Item
     * @param displayChar the character to use to represent this item if it is on the ground
     * @param portable true if and only if the Item can be picked up
     */
    public Waypoint(String name, char displayChar, boolean portable) {
        super(name, displayChar, portable);
    }

    public void addAction(Action action) {
        this.allowableActions.add(action);
    }
}
