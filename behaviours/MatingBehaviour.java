package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.actions.BreedAction;
import game.actors.Dinosaur;
import game.actors.Pterodactyl;
import game.ground.Tree;

import static java.lang.Math.*;

/**
 * A class for MatingBehaviour used by Dinosaurs to look for partners to breed with.
 *
 * @see game.behaviours.Behaviour
 */
public class MatingBehaviour implements Behaviour{

    /**
     * Returns an action for the dinosaur to do.
     * Inside the method, the dinosaur will search its surroundings for the closest suitable mating partner
     * and performs an action depending on how far it is from the mating partner.
     * MatingBehaviour will either return:
     * 1) null, when there is no nearby suitable mating partner.
     * 2) MoveActorAction, which moves the dinosaur towards the nearest suitable mating partner.
     * 3) BreedAction, which makes the dinosaurs breed.
     *
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return an Action class or null
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {

        boolean isPtero = ((Dinosaur) actor).getSpecies().equals("Pterodactyl");

        NumberRange fieldOfViewX = new NumberRange(max(map.locationOf(actor).x()-20,0), min(map.locationOf(actor).x()+20,map.getXRange().max())-max(map.locationOf(actor).x()-20,0));
        NumberRange fieldOfViewY = new NumberRange(max(map.locationOf(actor).y()-20,0), min(map.locationOf(actor).y()+20,map.getYRange().max())-max(map.locationOf(actor).y()-20,0));

        // find nearest potential mate
        Dinosaur nearestMate = null;
        for (int x : fieldOfViewX) {
            for (int y : fieldOfViewY){
                // run through actors
                if(map.isAnActorAt(map.at(x,y))) {
                    // check if same species
                    if (map.at(x, y).getActor().getClass().equals(actor.getClass())) {
                        Dinosaur dinosaur = (Dinosaur) map.at(x, y).getActor();
                        //check if opposite gender
                        if(!dinosaur.getGender().equals(((Dinosaur)actor).getGender())) {
                            // check if dinosaur is not pregnant
                            if(dinosaur.getInventory().size()==0) {
                                // for pterodactyls, check if mate is in a tree
                                if(isPtero){
                                    if(map.locationOf(dinosaur).getGround().getClass().equals(Tree.class)){
//                                        System.out.println("mate is in a tree");
                                        nearestMate = dinosaur;

                                    }//else
//                                    System.out.println("mate is not in a tree");
                                }else {
                                    nearestMate = dinosaur;
                                }
                            }
                        }
                    }
                }
            }
        }

        // for pterodactyl, if actor is not in tree
//        if(map.locationOf(actor).getGround().getClass().equals(Tree.class)){
//            nearestMate=null;
//        }

        // if no nearby mates, return null
        if (nearestMate==null){
            return null;
        }

        Location here = map.locationOf(actor);
        Location there = map.locationOf(nearestMate);

        int currentDistance = distance(here, there);
        if (currentDistance<=1) {
            if(isPtero){
                if(here.getGround().getClass().equals(Tree.class) && there.getGround().getClass().equals(Tree.class)){
                    return new BreedAction((Dinosaur) actor, nearestMate);
                }
            }else return new BreedAction((Dinosaur) actor, nearestMate);
        }
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
//                if (isPtero){ // for Pterodactyl, valid exits are trees around 'there'
//                    if (!destination.getGround().getClass().equals(Tree.class)){
//                        System.out.println("DEBUG: Destination was not a tree");
//                        continue;
//                    }else System.out.println("DEBUG: Destination is tree");
//                }
                int newDistance = distance(destination, there);
                if (newDistance < currentDistance) {
                    return new MoveActorAction(destination, exit.getName());
                }else if(isPtero){
                    if(destination.getGround().getClass().equals(Tree.class)){
                        System.out.println("Moving ptero towards tree");
                        return new MoveActorAction(destination, exit.getName());
                    }
                }
            }
        }

        // if no moves can put dinosaurs closer together
        return null;
    }

}
