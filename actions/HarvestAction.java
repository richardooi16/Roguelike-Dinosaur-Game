package game.actions;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.*;
import game.actors.Player;
import game.items.Fruit;
import game.ground.Bush;
import game.ground.Tree;

import java.util.Random;

/**
 * HarvestAction is given to player when it can harvest fruit from trees and bushes.
 *
 * @see edu.monash.fit2099.engine.Action
 * @see Player
 */
public class HarvestAction extends Action implements EcopointSource {

    private Random rand = new Random();

    /**
     * Constructor.
     */
    public HarvestAction() {
    }

    /**
     * Runs the action if player chooses it. The action removes a fruit from a tree or
     * bush and add it into player's inventory. This action has a chance of failing.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // check if ground is tree or bush
        if (map.locationOf(actor).getGround().getClass().equals(Tree.class)) {

            Tree tree = (Tree) map.locationOf(actor).getGround();

            if (tree.inTree.size()<1) {
                return "There are no fruits to harvest";
            }

            if (rand.nextDouble() < 0.4) {
                tree.inTree.remove(tree.inTree.size() - 1);
                actor.addItemToInventory(new Fruit());
                givePoints();
            } else {
                return actor + " fails to harvest";
            }

        } else if (map.locationOf(actor).getGround().getClass().equals(Bush.class)) {

            Bush bush = (Bush) map.locationOf(actor).getGround();

            if (bush.inBush.size()<1) {
                return "There are no fruits to harvest";
            }

            if (rand.nextDouble() < 0.4) {

                bush.inBush.remove(bush.inBush.size() - 1);
                actor.addItemToInventory(new Fruit());
                givePoints();
            } else {
                return actor + " fails to harvest";
            }
        }
        return actor + " successfully harvested";
    }

    /**
     * Returns a description for action.
     * @param actor The actor performing the action.
     * @return description for action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " attempts to harvest";
    }

    /**
     * Give player 10 ecopoints.
     */
    @Override
    public void givePoints() {
        Player.ecopoints += 10;
    }
}
