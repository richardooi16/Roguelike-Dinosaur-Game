package game.ground;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;

import java.util.Random;

/**
 * A class that represents bare dirt. Dirt has a chance to grow bushes on it.
 *
 * @see edu.monash.fit2099.engine.Ground
 */
public class Dirt extends Ground {

	private Random rand = new Random();

    /**
     * Constructor for Dirt.
     */
	public Dirt() {
		super('.');
	}

    /**
     * Dirt can also experience the joy of time.
     *
     * @param location The location of the Ground
     */
	@Override
	public void tick(Location location) {
		super.tick(location);
		growBush(location);
	}

    /**
     * Grows a bush with a percent chance of failure. If there is any Tree around, bush will not grow.
     * If there is at least 2 Bush objects around, bush has a higher chance to grow.
     *
     * @param location The location of the Ground
     */
	private void growBush(Location location) {
        double chance = rand.nextDouble();
        int bushesAround = 0;

        try {
            Location dNorth = location.map().at(location.x(), location.y() - 1);
            if (dNorth.getGround().getClass().equals(Tree.class)) {
                chance=0;
            } else if (dNorth.getGround().getClass().equals(Bush.class)) {
                bushesAround+=1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            Location dNorthEast = location.map().at(location.x() + 1, location.y() - 1);
            if (dNorthEast.getGround().getClass().equals(Tree.class)) {
                chance=0;
            } else if (dNorthEast.getGround().getClass().equals(Bush.class)) {
                bushesAround+=1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            Location dEast = location.map().at(location.x() + 1, location.y());
            if (dEast.getGround().getClass().equals(Tree.class)) {
                chance=0;
            } else if (dEast.getGround().getClass().equals(Bush.class)) {
                bushesAround+=1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            Location dSouthEast = location.map().at(location.x() + 1, location.y() + 1);
            if (dSouthEast.getGround().getClass().equals(Tree.class)) {
                chance=0;
            } else if (dSouthEast.getGround().getClass().equals(Bush.class)) {
                bushesAround+=1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            Location dSouth = location.map().at(location.x(), location.y() + 1);
            if (dSouth.getGround().getClass().equals(Tree.class)) {
                chance=0;
            } else if (dSouth.getGround().getClass().equals(Bush.class)) {
                bushesAround+=1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            Location dSouthWest = location.map().at(location.x() - 1, location.y() + 1);
            if (dSouthWest.getGround().getClass().equals(Tree.class)) {
                chance=0;
            } else if (dSouthWest.getGround().getClass().equals(Bush.class)) {
                bushesAround+=1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            Location dWest = location.map().at(location.x() - 1, location.y());
            if (dWest.getGround().getClass().equals(Tree.class)) {
                chance=0;
            } else if (dWest.getGround().getClass().equals(Bush.class)) {
                bushesAround+=1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            Location dNorthWest = location.map().at(location.x() - 1, location.y() - 1);
            if (dNorthWest.getGround().getClass().equals(Tree.class)) {
                chance=0;
            } else if (dNorthWest.getGround().getClass().equals(Bush.class)) {
                bushesAround+=1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {}


        if (bushesAround >= 2 && 1-chance<0.01) {
            location.setGround(new Bush());
        } else if(1-chance<0.001) {
            location.setGround(new Bush());
        }
    }
}
