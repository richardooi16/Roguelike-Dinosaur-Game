package game.actors;

import edu.monash.fit2099.engine.*;
import game.actions.AttackAction;
import game.behaviours.*;
import game.items.Corpse;

import java.util.Random;

/**
 * An abstract class to house all the important variables and methods for Dinosaurs.
 */
public abstract class Dinosaur extends Actor {

    protected Random random = new Random();
    protected String gender;
    protected int age;
    protected boolean isBaby;
    protected String species;
    protected int waterLevel;
    protected int maxWaterLevel;
    protected boolean unconscious=false;
    protected int unconsciousTurns;
    // 0=Wander, 1=Hungry, 2=Attack, 3=Mate, 4=Thirst
    protected final Behaviour[] behaviours = new Behaviour[4];
    protected int matureAge;

    /**
     * Constructor.
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the display
     * @param hitPoints   the Actor's starting hit points
     */
    public Dinosaur(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.waterLevel = 60;
//        this.waterLevel = 2;
        this.maxWaterLevel = 100;
        this.behaviours[0] = new WanderBehaviour();
        this.behaviours[1] = new HungryBehaviour();
        this.behaviours[2] = new MatingBehaviour();
        this.behaviours[3] = new ThirstyBehaviour();
        if(random.nextBoolean()){
            gender = "male";
        }
        else{
            gender = "female";
        }

    }

    /**
     * Constuctor.
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the display
     * @param hitPoints   the Actor's starting hit points
     * @param age         the age of the Dinosaur
     */
    public Dinosaur(String name, char displayChar, int hitPoints, int age) {
        super(name, displayChar, hitPoints);
        this.waterLevel = 60;
        this.maxWaterLevel = 100;
        this.behaviours[0] = new WanderBehaviour();
        this.behaviours[1] = new HungryBehaviour();
        this.behaviours[2] = new MatingBehaviour();
        this.behaviours[3] = new ThirstyBehaviour();
        if(random.nextBoolean()){
            gender = "male";
        }
        else{
            gender = "female";
        }
        // Dinosaur is constructed with a random age unless specified.
        this.age = age;
    }

    /**
     * Returns a collection of the Actions that the otherActor can do to the Dinosaur.
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return A collection of Actions.
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        return new Actions(new AttackAction(this));
    }

    /**
     * Removes the dinosaur from the map and replace it with a corpse.
     *
     * @param dinosaur The Dinosaur object
     * @param currentLocation The location where the Dinosaur died.
     */
    public void die(Dinosaur dinosaur, Location currentLocation){
        currentLocation.map().removeActor(dinosaur);
        currentLocation.addItem(new Corpse(dinosaur.name));
    }

    /**
     * Function that updates all dinosaur variables as the turn passes.
     */
    public void updateVariables() {
        this.waterLevel-=1;
        this.hitPoints-=1;
        this.age+=1;
    }

    /**
     * Returns true if water level less than 60.
     *
     * @return true if water level less than 60
     */
    public boolean isThirsty() { return this.waterLevel<60; }

    /**
     * Restores dinosaur's water level.
     *
     * @param waterLevel Water level to add/restore
     */
    public void restoreWaterLevel(int waterLevel) {
        this.waterLevel= Math.min(maxWaterLevel,waterLevel+this.waterLevel);
    }

    /**
     * Makes the dinosaur go unconscious. When the dinosaur is unconscious, it cannot move
     * for a set amount of turns.
     *
     * @param unconsciousTurns The amount of turns the dinosaur stays unconscious for
     */
    public void goUnconscious(int unconsciousTurns) {
        this.unconsciousTurns = unconsciousTurns;
        this.unconscious = true;
    }

    /**
     * Checks if the dinosaur goes unconscious or dies. If they are not unconscious, check their hitpoints and
     * water level if it is above 0. If they are 0 and below, make the dinosaur go unconscious. If the dinosaur is
     * unconscious, check if the turns is still above 0. If the turns is 0 and below, the dinosaur dies.
     *
     * @param map The map the dinosaur is in
     * @return DoNothingAction if unconscious or dead, else return null
     */
    public Action unconsciousOrDead(GameMap map) {
        if (unconscious && this.hitPoints>0 && this.waterLevel>=10) {
            unconscious = false;
            return null;
        }

        if (unconscious && this.hitPoints<=0) {
            System.out.println(this.name + " at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is unconscious from hunger" );
            this.hitPoints = 0;
            this.unconsciousTurns -=1;
            if (this.unconsciousTurns<=0){
                this.die(this, map.locationOf(this));
            }
            return new DoNothingAction();
        }

        if (unconscious && this.waterLevel<=0) {
            System.out.println(this.name + " at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is unconscious from thirst" );
            this.waterLevel = 0;
            this.unconsciousTurns -=1;
            if (this.unconsciousTurns<=0){
                this.die(this, map.locationOf(this));
            }
            return new DoNothingAction();
        }

        if(this.hitPoints <= 0 && !unconscious){
            int unconsciousTime = 20;
            if (this.species.equals("Stegosaur")
                    || this.species.equals("Allosaur")) {
                unconsciousTime = 20;
//                unconsciousTime = 30;   // to test recovering
//                unconsciousTime = 5;    // to test dying
            }
            else if (this.species.equals("Brachiosaur")) {
                unconsciousTime = 15;
            }

            this.goUnconscious(unconsciousTime);
            System.out.println(this.name + " at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is unconscious from hunger");

            return new DoNothingAction();
        }

        if(this.waterLevel <= 0 && !unconscious){
            this.goUnconscious(15);
            System.out.println(this.name + " at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is unconscious from thirst");

            return new DoNothingAction();
        }

        return null;
    }

    /**
     * Getter for species variable
     *
     * @return species of dinosaur.
     */
    public String getSpecies() {
        return species;
    }

    /**
     * Getter for gender variable
     *
     * @return gender of dinosaur.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Getter for max hit points variable
     *
     * @return max hit points of dinosaur.
     */
    public int getMaxHitPoints(){return maxHitPoints;}

    /**
     * Getter for hit points variable
     *
     * @return current hit points of dinosaur.
     */
    public int getHitPoints(){return hitPoints;}
}
