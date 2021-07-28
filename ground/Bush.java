package game.ground;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;
import game.items.Fruit;

import java.util.ArrayList;
import java.util.Random;

/**
 * A Bush Ground that can grow fruits. Players can harvest fruits from Bushes
 * and Stegosaurs can eat fruits from bushes. Brachiosaur can stomp Bushes and
 * destroy it.
 *
 * @see edu.monash.fit2099.engine.Ground
 */
public class Bush extends Ground{

    private Random rand = new Random();
    public ArrayList<Fruit> inBush = new ArrayList<Fruit>();

    /**
     * Constructor for Bush.
     */
    public Bush() {
        super('&');
    }

    /**
     * Bush can also experience the joy of time.
     *
     * @param location The location of the Ground
     */
    public void tick(Location location) {
        super.tick(location);

        growFruit();
    }

    /**
     * Grows a fruit in the Bush.
     */
    private void growFruit() {
        if (rand.nextDouble()<0.1) {
            inBush.add(new Fruit());
        }
    }
}
