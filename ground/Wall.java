package game.ground;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;

/**
 * A class that represents the floor inside a building.
 *
 * @see edu.monash.fit2099.engine.Ground
 */
public class Wall extends Ground {

	/**
	 * Constructor for wall.
	 */
	public Wall() {
		super('#');
	}

	/**
	 * Returns boolean showing whether an actor can enter this ground.
	 *
	 * @param actor the Actor to check
	 * @return false
	 */
	@Override
	public boolean canActorEnter(Actor actor) {
		return false;
	}

	/**
	 * Returns boolean showing whether thrown objects get blocked.
	 *
	 * @return true
	 */
	@Override
	public boolean blocksThrownObjects() {
		return true;
	}
}
