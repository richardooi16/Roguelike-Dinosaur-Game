package game.actions;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.actors.Dinosaur;
import game.ground.Tree;
import game.items.Egg;

/**
 * Action for Dinosaurs to breed. An egg will be placed into the female dinosaur's inventory
 * simulating pregnancy.
 *
 * @see edu.monash.fit2099.engine.Action
 * @see game.actors.Dinosaur
 */
public class BreedAction extends Action {

    Dinosaur maleDino, femaleDino;

    /**
     * Constructor.
     *
     * @param dinosaur1 One of the Dinosaurs mating
     * @param dinosaur2 One of the Dinosaurs mating
     */
    public BreedAction(Dinosaur dinosaur1, Dinosaur dinosaur2) {
        if (dinosaur1.getGender().equals("male")) {
            this.maleDino=dinosaur1;
            this.femaleDino=dinosaur2;
        } else {
            this.maleDino=dinosaur2;
            this.femaleDino=dinosaur1;
        }
    }

    /**
     * Perform the Action when called. This action creates an egg and placed into the female
     * dinosaur's inventory.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        boolean valid = true;
        if(maleDino.getSpecies().equals("Pterodactyl")) {
            if (!map.locationOf(maleDino).getGround().getClass().equals(Tree.class) || !map.locationOf(femaleDino).getGround().getClass().equals(Tree.class)){
                valid = false;
            }
        }
        if(valid) {
            this.femaleDino.addItemToInventory(new Egg(femaleDino.getSpecies(), femaleDino.getClass()));
        }
//        System.out.println("both parents are valid?: "+valid);
        return menuDescription(actor);
    }

    /**
     * Returns a descriptive string
     * @param actor The actor performing the action.
     * @return the text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return maleDino + "(male) breeds with " + femaleDino + "(female)";
    }
}
