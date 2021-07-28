package game.actors;

import edu.monash.fit2099.engine.*;
import game.ground.Lake;
import game.ground.Tree;
import game.items.Corpse;

/**
 * A carnivorous dinosaur that can eat corpses and fishes. Pterodactyl can also
 * fly over lakes and only breed on trees.
 */
public class Pterodactyl extends Dinosaur{

    boolean isFlying;
    int flyingTurns = 30;

    public boolean isFlying() {
        return isFlying;
    }
    public void toggleFly(boolean flying){ isFlying = flying; }

    /**
     * Constructor.
     * All Pterodactyls are represented by a 'P' and have 60 max hit points.
     *
     * @param name the name of this Pterodactyl
     */
    public Pterodactyl(String name) {
        super(name, 'P', 70);
        species = "Pterodactyl";
        maxHitPoints = 90;
        matureAge = 30;
        age = random.nextInt(61);
        isFlying = false;
    }

    public Pterodactyl(String name, String gender) {
        super(name, 'P', 1000);
        species = "Pterodactyl";
        maxHitPoints = 90;
        matureAge = 30;
//        age = random.nextInt(61);
        age = 40;
        isFlying = true;
        waterLevel = 100;
        this.gender = gender;
    }

    /**
     * Constructor.
     * All Pterodactyls are represented by a 'P' and have 60 max hit points.
     *
     * @param name The name of this Pterodactyl
     * @param hitPoints The starting hitpoints of this Pterodactyl
     * @param age The starting age of this Pterodactyl
     */
    public Pterodactyl(String name, int hitPoints, int age) {
        super(name, '*', hitPoints, age);
        species = "Pterodactyl";
        maxHitPoints = 90;
        matureAge = 30;
        isFlying = false;
        if(age < matureAge){
            this.isBaby = true;
            this.displayChar = 'p';
        }else{
            this.isBaby = false;
            this.displayChar = 'P';
        }
    }

    /**
     * This method runs a suitable behaviour depending on what the Pterodactyl is feeling now.
     * If the Pterodactyl is not flying, it will look for a tree, else if it wants to mate,
     * it will have the MatingBehaviour, else if it is Thirsty,
     * it will run a ThirstyBehavior, else if it is Hungry,
     * it will run a HungryBehavior, else it will run a WanderBehaviour. If WanderBehaviour fails,
     * it will run a doNothingAction.
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return The action to run
     *
     * @see edu.monash.fit2099.engine.Actor#playTurn(Actions, Action, GameMap, Display)
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        updateVariables();
        if(isFlying){
            flyingTurns-=1;
            if(flyingTurns == 0){
                toggleFly(false);
            }
        }
        if(map.locationOf(this).getGround().getClass().equals(Tree.class)){
            System.out.println("Ptero reached a tree");
            toggleFly(true);
            flyingTurns = 30;
        }

        Action action = unconsciousOrDead(map);
        if (action != null) {
            return action;
        }

        // Handle multi-turn Actions
        if (lastAction.getNextAction() != null)
            return lastAction.getNextAction();

        // Check if Pterodactyl is on a lake tile, if so replenish thirst() as well as grant a chance to find fish.
        if(map.locationOf(this).getGround().getClass().equals(Lake.class)){
            this.restoreWaterLevel(30);
            int x = 0;
            float chance = random.nextFloat();
            if(chance >= 0.7){
                x = 1;
                if(chance >= 0.9){
                    x = 2;
                }
            }
            if(((Lake) map.locationOf(this).getGround()).eatFish(x)){
                ((Lake) map.locationOf(this).getGround()).eatFish(x);
                this.heal(x*10);
                System.out.println("Pterodactyl at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") found "+x+" fish(es)");
            }
        }

        // check if dinosaur is on a Tree, since most of its food are from Lakes, as well as only being able to breed in trees.
        if(!isFlying){
            System.out.println("Pterodactyl wishes to fly");
            action = behaviours[0].getAction(this, map);
        }

        if (action != null){
            return action;
        }

        // check if dinosaur can breed
        if (this.hitPoints > 50 && this.inventory.size()==0 && this.age > 30) {
            System.out.println("Pterodactyl at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is healthy enough to breed");
            action = behaviours[2].getAction(this, map);
        }

        if (action != null) {
            return action;
        }

        // check if dinosaur is thirsty
        if (this.isThirsty()){
            System.out.println("Pterodactyl at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is thirsty");
            action = behaviours[3].getAction(this, map);
        }

        if (action != null) {
            return action;
        }

        // check if dinosaur is hungry
        else if (this.hitPoints < 50) {
            System.out.println("Pterodactyl at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is hungry");
            action = behaviours[1].getAction(this, map);
        }
        if (action != null) {
            return action;
        } else {
            action = behaviours[0].getAction(this, map);
        }

        if (action != null) {
            return action;
        } else {
            return new DoNothingAction();
        }
    }
}
