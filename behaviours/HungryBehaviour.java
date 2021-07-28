package game.behaviours;

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

import java.util.ArrayList;

import static java.lang.Math.*;

/**
 * A class for HungryBehaviour used by Dinosaurs to look for food.
 *
 * @see game.behaviours.Behaviour
 */
public class HungryBehaviour implements Behaviour{

    /**
     * Returns an action for the dinosaur to do.
     * Inside the method, the dinosaur will search its surroundings for the closest food source and
     * performs an action depending on how far it is from the food source.
     * HungryBehaviour will either return:
     * 1) null, when there is no nearby food source, or attackers are too close to other actors.
     * 2) MoveActorAction, which moves the dinosaur towards the nearest food source.
     * 3) EatAction, which makes the dinosaur eat from the food source.
     * 4) AttackAction, which makes the dinosaur attack and eat another living dinosaur
     *
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return an Action class or null
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        Class<? extends Actor> dinosaur;
        ArrayList<Class> foodSource = new ArrayList<>();
        NumberRange fieldOfViewX = new NumberRange(max(map.locationOf(actor).x()-20,0),
                min(map.locationOf(actor).x()+20,map.getXRange().max())-max(map.locationOf(actor).x()-20,0));
        NumberRange fieldOfViewY = new NumberRange(max(map.locationOf(actor).y()-20,0),
                min(map.locationOf(actor).y()+20,map.getYRange().max())-max(map.locationOf(actor).y()-20,0));

        boolean nearbyDino = false;
        dinosaur = actor.getClass();
        if (dinosaur.equals(Stegosaur.class)){
            foodSource.add(Bush.class);
            foodSource.add(Fruit.class);

        } else if (dinosaur.equals(Brachiosaur.class)) {
            foodSource.add(Tree.class);

        } else if (dinosaur.equals(Allosaur.class)) {
            foodSource.add(Stegosaur.class);
            foodSource.add(Pterodactyl.class);
            foodSource.add(Corpse.class);
            foodSource.add(Egg.class);

        } else if (dinosaur.equals(Pterodactyl.class)) {
            foodSource.add(Corpse.class);
            if(((Pterodactyl)actor).isFlying()){
                foodSource.add(Lake.class);
            }
        }

        // find nearest food source
        int shortestDist = -1;
        Location nearestFood = null;
        Class foodType = null;
        for (int x : fieldOfViewX) {
            for (int y : fieldOfViewY){
                // run through items
                for(Item item:map.at(x,y).getItems()) { // for item at map(iterable x,y)
                    if(foodSource.contains(item.getClass())) { // if item is in foodSource
                        if(Pterodactyl.class.isAssignableFrom(actor.getClass())){ // if actor is pterodactyl
                            Location foodLocale = map.at(x,y);
                            NumberRange nearbyX = new NumberRange(max(foodLocale.x()-10,0),
                                    min(foodLocale.x()+10,map.getXRange().max())-max(foodLocale.x()-10,0));
                            NumberRange nearbyY = new NumberRange(max(foodLocale.y()-10,0),
                                    min(foodLocale.y()+10,map.getYRange().max())-max(foodLocale.y()-10,0));

                            for (int horizontal : nearbyX) {
                                for (int vertical : nearbyY) {
                                    if(map.isAnActorAt(map.at(horizontal,vertical))){
                                        if(!map.getActorAt(map.at(horizontal, vertical)).getClass().isAssignableFrom(Pterodactyl.class)){
                                            nearbyDino = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(nearbyDino){
                                continue;
                            }
                        }

                        int dist = distance(map.locationOf(actor), map.at(x,y));

                        if (dist<shortestDist || shortestDist == -1) {
                            shortestDist = dist;
                            nearestFood = map.at(x,y);
                            foodType = item.getClass();
                        }
                    }
                }
                // run through ground
                if(foodSource.contains(map.at(x,y).getGround().getClass())) {
                    if (map.at(x,y).getGround().getDisplayChar() == '&') {
                        Bush bush = (Bush) map.at(x,y).getGround();
                        if (bush.inBush.isEmpty()){
                            continue;
                        }
                    }
                    else if (map.at(x,y).getGround().getDisplayChar() == 'T'
                            || map.at(x,y).getGround().getDisplayChar() == 't') {
                        Tree tree = (Tree) map.at(x,y).getGround();
                        if (tree.inTree.isEmpty()){
                            continue;
                        }
                    }

                    int dist = distance(map.locationOf(actor), map.at(x,y));

                    if (dist<shortestDist || shortestDist == -1) {
                        shortestDist = dist;
                        nearestFood = map.at(x,y);
                        foodType = map.at(x,y).getGround().getClass();
                    }
                }
                // run through actors
                if(map.isAnActorAt(map.at(x,y))) {
                    if (foodSource.contains(map.at(x, y).getActor().getClass())) {
                        if(((Allosaur) actor).onCooldown(map.at(x, y).getActor())){
                            continue;
                        }
                        if(map.at(x,y).getActor().getClass().equals(Pterodactyl.class)){
                            if(((Pterodactyl) map.at(x,y).getActor()).isFlying()){
                                continue;
                            }
                        }
                        int dist = distance(map.locationOf(actor), map.at(x, y));

                        if (dist < shortestDist || shortestDist == -1) {
                            shortestDist = dist;
                            nearestFood = map.at(x, y);
                            foodType = map.at(x, y).getActor().getClass();
                        }
                    }
                }
            }
        }

        // if no nearby food
        if (nearestFood==null){
            return null;
        }

        if (Dinosaur.class.isAssignableFrom(foodType) && shortestDist<=2) {
            for (Exit exit:map.locationOf(actor).getExits()) {
                Location destination = exit.getDestination();
                if (destination.containsAnActor()) {
                    return new AttackAction(nearestFood.getActor());
                }
            }
        }

        // if dinosaur on food source
        else if (shortestDist==0) {
            return new EatAction();
        }

        // find exit that puts dinosaur closer to food source
        for (Exit exit:map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                int newDist = distance(destination,nearestFood);

                if (newDist<shortestDist) {
                    if (Dinosaur.class.isAssignableFrom(foodType) && newDist==0) {
                        continue;
                    }
                    else {
                        System.out.println("move towards food");
                        return new MoveActorAction(destination, exit.getName());
                    }
                }
            }
        }

        // no exit that puts dinosaur closer to food source
        return null;
    }

}

