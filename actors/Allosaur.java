package game.actors;

import edu.monash.fit2099.engine.*;
import game.items.Corpse;
import game.items.Egg;

import java.util.ArrayList;

/**
 * A carnivorous dinosaur that can eat other Dinosaurs and corpses.
 */
public class Allosaur extends Dinosaur{

//    private final Behaviour[] behaviours = new Behaviour[3];
    private ArrayList<Actor> cooldownName = new ArrayList<>();
    private ArrayList<Integer> cooldownTime = new ArrayList<>();

    /**
     * Constructor.
     * All Allosaurs are represented by a 'A' and have 135 max hit points.
     *
     * @param name        the name of the Actor
     */
    public Allosaur(String name) {

        super(name, 'A', 70);
        species = "Allosaur";
        maxHitPoints = 135;
        matureAge = 50;
        // 0=Wander, 1=Hungry, 2=Attack, 3=Mate
//        behaviours[0] = new WanderBehaviour();
//        behaviours[1] = new HungryBehaviour();
//        behaviours[2] = new MatingBehaviour();

        age = random.nextInt(100);
    }

    /**
     * Constructor.
     * All Allosaurs are represented by a 'A' and have 135 max hit points.
     *
     * @param name        the name of the Actor
     * @param hitPoints   the Actor's starting hit points
     * @param age         the age of the Dinosaur
     */
    public Allosaur(String name, int hitPoints, int age) {

        super(name, 'A', hitPoints, age);
        species = "Allosaur";

        maxHitPoints = 135;
        matureAge = 50;
//        behaviours[0] = new WanderBehaviour();
//        behaviours[1] = new HungryBehaviour();
//        behaviours[2] = new MatingBehaviour();

        if(age < matureAge){
            this.isBaby = true;
            this.displayChar = 'a';
        }else{
            this.isBaby = false;
            this.displayChar = 'A';
        }
    }

    /**
     * Should increase the Allosaur’s food level depending on the corpse type.
     *
     * @param currentLocation The location of the corpse.
     * @return true if the Allosaur successfully eats a corpse, else return false
     */
    public boolean eatCorpse(Location currentLocation) {
        boolean eaten = false;
        for(Item item : currentLocation.getItems()){
            if(item.getClass().equals(Corpse.class)){
                String type = ((Corpse) item).getType();
                if(type.equals("Stegosaur") || type.equals("Allosaur")){
                    this.heal(50);
                    currentLocation.removeItem(item);
                    eaten = true;
                    break;
                }else if(type.equals("Brachiosaur")){
                    this.heal(9999);
                    currentLocation.removeItem(item);
                    eaten = true;
                    break;
                }else if(type.equals("Pterodactyl")){
                    this.heal(30);
                    currentLocation.removeItem(item);
                    eaten = true;
                    break;
                }
            }
        }
        return eaten;
    }

    /**
     * Egg should disappear and should increase the Allosaur’s food level by 10.
     *
     * @param currentLocation The location of the egg.
     * @return true if the Allosaur successfully eats an egg, else return false
     */
    public boolean eatEgg(Location currentLocation) {
        boolean eaten = false;
        for(Item item : currentLocation.getItems()){
            if(item.getClass().equals(Egg.class)){
                currentLocation.removeItem(item);
                this.heal(10);
                eaten = true;
                break;
            }
        }
        return eaten;
    }

    /**
     * Gives the Allosaur an intrinsic weapon.
     *
     * @return An intrinsic weapon object
     */
    @Override
    protected IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(20, "bites");
    }

    /**
     * This method runs a suitable behaviour depending on what the Allosaur is feeling now.
     * If the Allosaur wants to mate, it will have the MatingBehaviour, else if it is thirsty, it will
     * run the ThirstyBehaviour, else if it is Hungry, it will run a HungryBehavior, else it will run
     * a WanderBehaviour. If WanderBehaviour fails, it will run a doNothingAction.
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

        Action action = unconsciousOrDead(map);

        if (action != null) {
            return action;
        }

        // decrease each cooldown timer by 1
        if (cooldownTime.size() != 0){
            for(int slot = 0; slot < cooldownTime.size(); slot++){
                cooldownTime.set(slot, cooldownTime.get(slot)-1);
                if(cooldownTime.get(slot) == 0){
                    cooldownName.remove(slot);
                    cooldownTime.remove(slot);
                }
            }
        }

        // check if dinosaur can breed
        if (this.hitPoints > 90 && this.inventory.size()==0 && this.age > 50) {
            System.out.println("Allosaur at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is healthy enough to breed");
            action = behaviours[2].getAction(this, map);
        }

        if (action != null) {
            return action;
        }

        // check if dinosaur is thirsty
        if (this.isThirsty()){
            System.out.println("Allosaur at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is thirsty");
            action = behaviours[3].getAction(this, map);
        }

        if (action != null) {
            return action;
        }

        // check if dinosaur is hungry
        else if (this.hitPoints < 90) {
            System.out.println("Allosaur at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is hungry");
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

    /**
     * Returns true if target dinosaur cannot be attacked.
     * @param dinosaur The dinosaur to be attacked
     * @return true if dinosaur is in cooldown arraylist, else return false
     */
    public boolean onCooldown(Actor dinosaur){
        return cooldownName.contains(dinosaur);
    }

    /**
     * Adds dinosaur to the cooldownName arraylist and add time to the cooldownTime arraylist.
     * @param dinosaur The dinosaur that was attacked
     */
    public void addCooldown(Actor dinosaur){
        cooldownName.add(dinosaur);
        cooldownTime.add(20);
    }

    /**
     * Returns time remaining for the attack cooldown period of the dinosaur.
     * @param dinosaur The dinosaur that was attacked
     * @return The time remaining for the Dinosaur to be open to attacks
     */
    public int cooldownPeriod(Actor dinosaur){
//        for(int i = 0; i < cooldownName.size(); i++){
//            System.out.println(cooldownName.get(i).getDisplayChar()+" | "+cooldownTime.get(i));
//        }

        int slot = cooldownName.indexOf(dinosaur);
        int time = cooldownTime.get(slot);
        return time;
    }
}
