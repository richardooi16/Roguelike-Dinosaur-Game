package game.behaviours;

import java.util.ArrayList;
import java.util.Random;

import edu.monash.fit2099.engine.*;
import game.actions.AttackAction;
import game.actions.EatAction;
import game.actors.*;
import game.ground.Bush;
import game.ground.Lake;
import game.ground.Tree;
import game.items.Corpse;
import game.items.Egg;
import game.items.Fruit;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * A class for WanderBehaviour used by Dinosaurs to move around the map.
 *
 * @see game.behaviours.Behaviour
 */
public class WanderBehaviour implements Behaviour {
	
	private Random random = new Random();


	/**
	 * Returns a MoveAction to wander to a random location, if possible.
	 * If no movement is possible, returns null.
	 * 
	 * @param actor the Actor enacting the behaviour
	 * @param map the map that actor is currently on
	 * @return an Action, or null if no MoveAction is possible
	 */
	@Override
	public Action getAction(Actor actor, GameMap map) {
		ArrayList<Action> actions = new ArrayList<Action>();

		Class<? extends Actor> dinosaur;
		dinosaur = actor.getClass();
		NumberRange fieldOfViewX;
		NumberRange fieldOfViewY;

		if(dinosaur.equals(Pterodactyl.class)){
			fieldOfViewX = new NumberRange(max(map.locationOf(actor).x() - 40, 0),
					min(map.locationOf(actor).x() + 40, map.getXRange().max()) - max(map.locationOf(actor).x() - 40, 0));
			fieldOfViewY = new NumberRange(max(map.locationOf(actor).y() - 40, 0),
					min(map.locationOf(actor).y() + 40, map.getYRange().max()) - max(map.locationOf(actor).y() - 40, 0));
		}else {
			fieldOfViewX = new NumberRange(max(map.locationOf(actor).x() - 20, 0),
					min(map.locationOf(actor).x() + 20, map.getXRange().max()) - max(map.locationOf(actor).x() - 20, 0));
			fieldOfViewY = new NumberRange(max(map.locationOf(actor).y() - 20, 0),
					min(map.locationOf(actor).y() + 20, map.getYRange().max()) - max(map.locationOf(actor).y() - 20, 0));
		}

		if (dinosaur.equals(Pterodactyl.class)) {

			// find nearest tree
			int shortestDist = -1;
			Location nearestTree = null;
			for (int x : fieldOfViewX) {
				for (int y : fieldOfViewY) {

					// run through ground
					if (map.at(x, y).getGround().getDisplayChar() == 'T'
							|| map.at(x, y).getGround().getDisplayChar() == 't') {

						int dist = distance(map.locationOf(actor), map.at(x, y));

						if (dist < shortestDist || shortestDist == -1) {
							shortestDist = dist;
							nearestTree = map.at(x, y);
						}
					}
				}
			}
			if (shortestDist==0) {
				return new DoNothingAction();
			}

			// find exit that puts dinosaur closer to Tree
			for (Exit exit:map.locationOf(actor).getExits()) {
				Location destination = exit.getDestination();
				if (destination.canActorEnter(actor) && nearestTree!=null) {
					int newDist = distance(destination,nearestTree);

					if (newDist<shortestDist) {
						System.out.println("move towards Tree");
						return new MoveActorAction(destination, exit.getName());

					}
				}
			}
		}

		for (Exit exit : map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
            	actions.add(exit.getDestination().getMoveAction(actor, "around", exit.getHotKey()));
            }
        }

		if (!actions.isEmpty()) {
			return actions.get(random.nextInt(actions.size()));
		}
		else {
			return null;
		}

	}

}
