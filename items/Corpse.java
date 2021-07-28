package game.items;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import game.PortableItem;
import game.actors.Dinosaur;

/**
 * A class for Corpse item. A Corpse is created when a Dinosaur dies. Allosaurs can eat corpses to
 * regain health. After some time, the corpses will decompose and be removed from the map. The
 * decomposition time depends on the type of dinosaur that died.
 *
 * @see game.PortableItem
 * @see Dinosaur
 */
public class Corpse extends PortableItem {
    int decompositionTime;
    String type;

    /**
     * Constructor for corpse.
     *
     * @param name The species of corpse
     */
    public Corpse(String name) {
        super(name, 'x');
        type = name;
        if(this.name.equals("Stegosaur") || this.name.equals("Allosaur")){
            decompositionTime = 21;     // +1 such that it does not start rotting on the same turn that it dies.
        }else if(this.name.equals("Brachiosaur")){
            decompositionTime = 41;     // +1 such that it does not start rotting on the same turn that it dies.
        }else if(this.name.equals("Pterodactyl")){
            decompositionTime = 21;     // +1 such that it does not start rotting on the same turn that it dies.
        }
    }

    /**
     * Returns the type/species of corpse.
     *
     * @return type/species of corpse
     */
    public String getType() {
        return type;
    }

    /**
     * Inform corpse of the passage of time when in an inventory.
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
     * Inform corpse on the ground of the passage of time.
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
     * Decrements the decomposition time.
     */
    public void rot() {
        this.decompositionTime -= 1;
    }

    /**
     * Returns true is the Corpse is rotten.
     *
     * @return true if Corpse is rotten, else return false
     */
    public boolean isRotten() {
        return this.decompositionTime == 0;
    }
}
