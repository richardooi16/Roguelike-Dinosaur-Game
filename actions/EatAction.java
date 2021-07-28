package game.actions;

import edu.monash.fit2099.engine.*;
import game.actors.Allosaur;
import game.actors.Brachiosaur;
import game.actors.Pterodactyl;
import game.actors.Stegosaur;
import game.items.Corpse;

/**
 * EatAction is given to Dinosaur when it can eat food from their food source.
 *
 * @see edu.monash.fit2099.engine.Action
 * @see game.actors.Dinosaur
 */
public class EatAction extends Action {
    int pEatCorpseTurns = 0;
    boolean pEatCorpseDone = true;

    /**
     * Perform the Action when called. This action heals the dinosaur and sometimes remove
     * the item.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Location currentLocation = map.locationOf(actor);
        boolean eaten = false;
        if(actor.getClass().equals(Stegosaur.class)) {
            eaten = ((Stegosaur) actor).eatFruit(actor, currentLocation);
            ((Stegosaur) actor).eatFruit(actor, currentLocation);
        }
        else if(actor.getClass().equals(Brachiosaur.class)) {
            eaten = ((Brachiosaur) actor).eatFruit(actor, currentLocation);
            ((Brachiosaur) actor).eatFruit(actor, currentLocation);
        }
        else if(actor.getClass().equals(Allosaur.class)) {  // check ground for corpse first, then attack stegosaur if no corpses.
            eaten = ((Allosaur) actor).eatCorpse(currentLocation);
            ((Allosaur) actor).eatCorpse(currentLocation);
            if (!eaten) {
                eaten = ((Allosaur) actor).eatEgg(currentLocation);
                ((Allosaur) actor).eatEgg(currentLocation);
            }
        }
        else if(actor.getClass().equals(Pterodactyl.class)) {
//            eaten = ((Pterodactyl) actor).eatCorpse(currentLocation);
//            ((Pterodactyl) actor).eatCorpse(currentLocation);

            for(Item item : currentLocation.getItems()) {
                if (item.getClass().equals(Corpse.class)) {
                    String type = ((Corpse) item).getType();
                    // First, check if Pterodactyl is currently eating a corpse
                    if (pEatCorpseTurns == 0){
                        pEatCorpseDone = true;
                    }
                    // If it is not eating a corpse, check for new corpse, set pEatCorpseDone to false and set the appropriate turns
                    if (pEatCorpseDone) {
                        if (type.equals("Stegosaur") || type.equals("Allosaur")) {
                            pEatCorpseTurns = 5;
                            pEatCorpseDone = false;
                            ((Pterodactyl) actor).toggleFly(false);

                        } else if (type.equals("Brachiosaur")) {   // if Actor has 47 hit points, it would take 5 turns to heal to full @ 10 points per turn.
                            pEatCorpseTurns = (((Pterodactyl) actor).getMaxHitPoints() - ((Pterodactyl) actor).getHitPoints()) / 10 + 1;
                            pEatCorpseDone = false;
                            ((Pterodactyl) actor).toggleFly(false);
                        }
                        System.out.println("Pterodactyl has begun eating a(n) "+type+" corpse.");
                    }else{  // reduce remaining turns by 1.
                        pEatCorpseTurns -= 1;
                    }
                    actor.heal(10);
                    if(pEatCorpseTurns == 0){
                        currentLocation.removeItem(item);
                        pEatCorpseDone = true;
                        System.out.println("Pterodactyl has finished eating a(n) "+type+" corpse.");
                        break;
                    }
                    eaten = true;
                }
            }
        }
        if(eaten) {
            return menuDescription(actor);
        }else return actor+" did not find any ground food.";
    }

    /**
     * Returns a descriptive string
     * @param actor The actor performing the action.
     * @return the text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " ate food";
    }

    /**
     * This provides a mechanism for Actions to take more than one turn.
     * For example, an action can change its state and return itself, or return the next Action in a series.
     * By default, this returns null, indicating that the Action will complete in one turn.
     *
     * @return null
     */
    @Override
    public Action getNextAction() {
        if (!pEatCorpseDone)
            return this;

        return null;
    }
}
