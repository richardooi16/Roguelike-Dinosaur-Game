package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.actions.DrinkAction;
import game.ground.Lake;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * A class for ThirstyBehaviour used by Dinosaurs to look for water.
 *
 * @see game.behaviours.Behaviour
 */
public class ThirstyBehaviour implements Behaviour {
    /**
     * Returns an action for the dinosaur to do.
     * Inside the method, the dinosaur will search its surroundings for the closest water source and
     * performs an action depending on how far it is from the water source.
     * ThirstyBehaviour will either return:
     * 1) null, when there is no nearby food source, or attackers are too close to other actors.
     * 2) MoveActorAction, which moves the dinosaur towards the nearest food source.
     * 3) DrinkAction, which makes the dinosaur drink from the water source.
     *
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return an Action class or null
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        NumberRange fieldOfViewX = new NumberRange(max(map.locationOf(actor).x() - 20, 0),
                min(map.locationOf(actor).x() + 20, map.getXRange().max()) - max(map.locationOf(actor).x() - 20, 0));
        NumberRange fieldOfViewY = new NumberRange(max(map.locationOf(actor).y() - 20, 0),
                min(map.locationOf(actor).y() + 20, map.getYRange().max()) - max(map.locationOf(actor).y() - 20, 0));


        int shortestDist = -1;
        Location nearestWater = null;

        for (int x : fieldOfViewX) {
            for (int y : fieldOfViewY) {
                // run through ground
                if (map.at(x, y).getGround().getDisplayChar() == '~') {
                    Lake lake = (Lake) map.at(x, y).getGround();
                    if (lake.getSips()==0) {
                        continue;
                    }

                    int dist = distance(map.locationOf(actor), map.at(x, y));

                    if (dist < shortestDist || shortestDist == -1) {
                        shortestDist = dist;
                        nearestWater = map.at(x, y);
                    }
                }

            }
        }

        // if no nearby water
        if (nearestWater == null) {
            return null;
        }

        for (Exit exit:map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();
            if (destination.getGround().getDisplayChar() == '~') {
                return new DrinkAction(exit.getDestination());
            }
        }

        // find exit that puts dinosaur closer to water source
        for (Exit exit:map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                int newDist = distance(destination,nearestWater);

                if (newDist<shortestDist) {
                    if (actor.getDisplayChar()!='P' && newDist==0) {
                        continue;
                    }
                    else {
                        System.out.println("move towards water");
                        return new MoveActorAction(destination, exit.getName());
                    }
                }
            }
        }

        // no exit that puts dinosaur closer to water source
        return null;
    }
}
