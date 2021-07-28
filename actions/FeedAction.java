package game.actions;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import game.EcopointSource;
import game.actors.*;
import game.items.CarnivoreMealKit;
import game.items.Fruit;
import game.items.VegetarianMealKit;
import game.items.WaterBottle;

import java.util.Scanner;

/**
 * FeedAction is given to player when it can feed a nearby Dinosaur.
 *
 * @see edu.monash.fit2099.engine.Action
 * @see Player
 */
public class FeedAction extends Action implements EcopointSource {

    Dinosaur dinosaur;

    /**
     * Constuctor.
     *
     * @param dinosaur The target Dinosaur to feed
     */
    public FeedAction(Actor dinosaur){
        this.dinosaur = (Dinosaur) dinosaur;
    }

    /**
     * Runs the action if player chooses it. The action removes an item from a player's
     * inventory and feeds a Dinosaur with it. The dinosaur gets healed or hydrated depending
     * on what item was fed to it.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        int input = menuInventory(actor);
        if (input == 0) {
            return "No items in inventory";
        }
        Item item = actor.getInventory().get(input-1);
        if ((dinosaur.getClass().equals(Stegosaur.class) || (dinosaur.getClass().equals(Brachiosaur.class)))
                && item.getClass().equals(Fruit.class)) {
            dinosaur.heal(20);
            givePoints();
        }
        else if ((dinosaur.getClass().equals(Stegosaur.class) || (dinosaur.getClass().equals(Brachiosaur.class)))
                && item.getClass().equals(VegetarianMealKit.class)) {
            dinosaur.heal(9999);
        }
        else if ((dinosaur.getClass().equals(Allosaur.class))
                && item.getClass().equals(CarnivoreMealKit.class)) {
            dinosaur.heal(9999);
        }
        else if (item.getClass().equals(WaterBottle.class)) {
            dinosaur.restoreWaterLevel(100);
        }
        else {
            return "item not suitable";
        }
        actor.removeItemFromInventory(item);
        return menuDescription(actor);
    }

    /**
     * Returns a description for action.
     * @param actor The actor performing the action.
     * @return description for action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " feeds " + dinosaur;
    }

    /**
     * Prints the player's inventory and prompts the user to input which item the player will use
     * to feed the Dinosaur.
     * @param player
     * @return choice of item from player's inventory
     */
    public int menuInventory(Actor player) {
        String out = "Player's inventory:\n";
        int counter = 1;
        int input = 0;
        for (Item i: player.getInventory()) {
            out += counter + ") " + i.toString() + "\n";
            counter+=1;
        }
        System.out.println(out);

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Option: ");
            input = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid Input");
        }

        return input;
    }

    /**
     * Give player 10 ecopoints.
     */
    @Override
    public void givePoints() {
        Player.ecopoints += 10;
    }
}
