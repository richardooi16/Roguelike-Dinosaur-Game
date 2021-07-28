package game.ground;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;
import game.EcopointSource;
import game.items.Fruit;
import game.actors.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * A Tree Ground that can grow fruits. Players can harvest fruits from Trees and Brachiosaur
 * can eat fruits from Trees. Trees have an age where the display character changes
 * depending on the age of the tree.
 *
 * @see edu.monash.fit2099.engine.Ground
 */
public class Tree extends Ground implements EcopointSource {
	private int age = 0;
	private Random rand = new Random();
	public ArrayList<Fruit> inTree = new ArrayList<Fruit>();

	/**
	 * Constructor.
	 */
	public Tree() {
		super('+');
	}

	/**
	 * Tree can also experience the joy of time.
	 *
	 * @param location The location of the Ground
	 */
	@Override
	public void tick(Location location) {
		super.tick(location);

		age++;
		if (age == 10)
			displayChar = 't';
		if (age == 20)
			displayChar = 'T';

		dropFruit(location);
		growFruit();
	}

	/**
	 * Grows a fruit in the Tree.
	 */
	private void growFruit() {
		if (rand.nextDouble()<0.5) {
			inTree.add(new Fruit());
			givePoints();
		}
	}

	/**
	 * Drops a fruit from inside the Tree to the Ground.
	 *
	 * @param location The location of the tree
	 */
	private void dropFruit(Location location) {
		double chance = rand.nextDouble();

		if (rand.nextDouble()<0.05 && !inTree.isEmpty()) {
			inTree.remove(inTree.size()-1);
			location.addItem(new Fruit(15));
		}

	}

	/**
	 * Give player 1 ecopoint.
	 */
	@Override
	public void givePoints() {
		Player.ecopoints += 1;
	}

}
