package game.behaviours;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.MoveActorAction;

/**
 * A class that figures out a MoveAction that will move the actor one step 
 * closer to a target Actor.
 *
 * @see game.behaviours.Behaviour
 */
public class FollowBehaviour implements Behaviour {

	private Actor target;

	/**
	 * Constructor.
	 * 
	 * @param subject the Actor to follow
	 */
	public FollowBehaviour(Actor subject) {
		this.target = subject;
	}

	/**
	 * Returns an action for the dinosaur to do.
	 * This method will move the dinosaur closer to the other actor by computing the Manhattan
	 * distance between them.
	 *
	 * @param actor the Actor acting
	 * @param map the GameMap containing the Actor
	 * @return
	 */
	@Override
	public Action getAction(Actor actor, GameMap map) {
		if(!map.contains(target) || !map.contains(actor))
			return null;
		
		Location here = map.locationOf(actor);
		Location there = map.locationOf(target);

		int currentDistance = distance(here, there);
		for (Exit exit : here.getExits()) {
			Location destination = exit.getDestination();
			if (destination.canActorEnter(actor)) {
				int newDistance = distance(destination, there);
				if (newDistance < currentDistance) {
					return new MoveActorAction(destination, exit.getName());
				}
			}
		}

		return null;
	}

}