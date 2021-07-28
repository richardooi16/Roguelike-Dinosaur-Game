package game.actions;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.actors.Player;
import game.items.*;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Action for Player to buy items from the vending machine.
 *
 * @see edu.monash.fit2099.engine.Action
 * @see Player
 */
public class BuyAction extends Action {

    String item = "item";

    /**
     * Runs the action if player chooses it. The action buys an item from the vending
     * machine using ecopoints and adds the item bought into the inventory.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        int input;
        do {
            input = buyOptions();
            switch (input){
                case 1:
                    Player.ecopoints -= 30;
                    actor.addItemToInventory(new Fruit());
                    System.out.println("Fruits bought");
                    item = "Fruits";
                    input = -1;
                    break;

                case 2:
                    Player.ecopoints -= 100;
                    actor.addItemToInventory(new VegetarianMealKit());
                    System.out.println("Vegetarian Meal Kit bought");
                    item = "Vegetarian Meal Kit";
                    input = -1;
                    break;

                case 3:
                    Player.ecopoints -= 500;
                    actor.addItemToInventory(new CarnivoreMealKit());
                    System.out.println("Carnivore Meal Kit bought");
                    item = "Carnivore Meal Kit";
                    input = -1;
                    break;

                case 4:
                    Player.ecopoints -= 200;
                    actor.addItemToInventory(new Egg("Stegosaur", actor.getClass()));
                    System.out.println("Stegosaur Egg bought");
                    item = "Stegosaur Egg";
                    input = -1;
                    break;

                case 5:
                    Player.ecopoints -= 500;
                    actor.addItemToInventory(new Egg("Brachiosaur", actor.getClass()));
                    System.out.println("Brachiosaur Egg bought");
                    item = "Brachiosaur Egg";
                    input = -1;
                    break;

                case 6:
                    Player.ecopoints -= 1000;
                    actor.addItemToInventory(new Egg("Allosaur", actor.getClass()));
                    System.out.println("Allosaur Egg bought");
                    item = "Allosaur Egg";
                    input = -1;
                    break;

                case 7:
                    Player.ecopoints -= 200;
                    actor.addItemToInventory(new Egg("Pterodactyl", actor.getClass()));
                    System.out.println("Pterodactyl Egg bought");
                    item = "Pterodactyl Egg";
                    input = -1;
                    break;

                case 8:
                    Player.ecopoints -= 500;
                    actor.addItemToInventory(new LaserGun());
                    System.out.println("Laser Gun bought");
                    item = "Laser Gun";
                    input = -1;
                    break;

                case 9:
                    Player.ecopoints -= 100;
                    actor.addItemToInventory(new WaterBottle());
                    System.out.println("Water Bottle bought");
                    item = "Water Bottle";
                    input = -1;
                    break;

                case 10:
                    return actor + " does not buy anything from Vending Machine";
            }
        } while (input!=-1);

        return menuDescription(actor);
    }

    /**
     * Returns a descriptive string.
     * @param actor The actor performing the action.
     * @return the text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " buys " + item + " from Vending Machine";
    }

    /**
     * Prints the Vending Machine menu for the player to choose what item to buy.
     *
     * @return The choice of item made by the player.
     */
    private int buyOptions(){
        int input=0;

        System.out.println("___________________________");
        System.out.println("| Vending Machine options |");
        System.out.println("___________________________");
        System.out.println("1) Fruits (30 points)");
        System.out.println("2) Vegeterian Meal Kit (100 points)");
        System.out.println("3) Carnivore Meal Kit (500 points)");
        System.out.println("4) Stegosaur Egg (200 points)");
        System.out.println("5) Branchiosaur Egg (500 points)");
        System.out.println("6) Allosaur Egg (1000 points)");
        System.out.println("7) Pterodactyl Egg (200 points)");
        System.out.println("8) Laser Gun (500 points)");
        System.out.println("9) Water Bottle (100 points)");
        System.out.println("10) Cancel Buy");
        System.out.println("___________________________");

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Option: ");
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
            // input = 0
        }

        return input;
    }
}
