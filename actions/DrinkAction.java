package game.actions;

import edu.monash.fit2099.engine.*;
import game.actors.Dinosaur;
import game.ground.Lake;

/**
 * DrinkAction is given to Dinosaur when it can drink water from the water source.
 *
 * @see edu.monash.fit2099.engine.Action
 * @see game.actors.Dinosaur
 */
public class DrinkAction extends Action {

    Location waterLocation;

    public DrinkAction(Location location) {
        this.waterLocation = location;
    }

    /**
     * Perform the Action. This action restores the dinosaur's water level and reduces
     * sips of lake.
     *
     * @param actor The actor performing the action.
     * @param map   The map the actor is on.
     * @return a description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Dinosaur dinosaur = (Dinosaur) actor;
        if (dinosaur.getSpecies().equals("Brachiosaur")){
            dinosaur.restoreWaterLevel(80);
        } else {
            dinosaur.restoreWaterLevel(30);
        }
        Lake lake = (Lake) waterLocation.getGround();
        lake.setSips(lake.getSips()-1);
        return menuDescription(actor);
    }

    /**
     * Returns a descriptive string
     *
     * @param actor The actor performing the action.
     * @return the text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " drank water";
    }
}
