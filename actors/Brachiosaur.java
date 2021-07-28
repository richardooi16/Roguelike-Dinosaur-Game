package game.actors;

import edu.monash.fit2099.engine.*;
import game.actions.AttackAction;
import game.behaviours.*;
import game.ground.Bush;
import game.ground.Dirt;
import game.ground.Tree;

/**
 * A herbivorous dinosaur that can eat from trees. Brachiosaurs can also stomp and kill bushes.
 */
public class Brachiosaur extends Dinosaur{

    // 0=Wander, 1=Hungry, 2=Attack, 3=Mate
//    private final Behaviour[] behaviours = new Behaviour[3];

    /**
     * Constructor.
     * All Brachiosaurs are represented by a 'B' and have 160 max hit points.
     *
     * @param name        the name of the Actor
     */
    public Brachiosaur(String name) {

        super(name, 'B', 100);
        species = "Brachiosaur";
        maxHitPoints = 160;
        maxWaterLevel = 200;
        matureAge = 50;
//        behaviours[0] = new WanderBehaviour();
//        behaviours[1] = new HungryBehaviour();
//        behaviours[2] = new MatingBehaviour();

        // Dinosaur is constructed with a random age unless specified.
        age = random.nextInt(100);
    }

    /**
     * Constructor.
     * All Brachiosaurs are represented by a 'B' and have 160 max hit points.
     *
     * @param name        the name of the Actor
     * @param hitPoints   The hitpoints of this Brachiosaur
     * @param age         The age of this Brachiosaur
     */
    public Brachiosaur(String name,int hitPoints, int age) {

        super(name, 'B', hitPoints, age);
        species = "Brachiosaur";
        maxHitPoints = 160;
        maxWaterLevel = 200;
        matureAge = 50;
//        behaviours[0] = new WanderBehaviour();
//        behaviours[1] = new HungryBehaviour();
//        behaviours[2] = new MatingBehaviour();

        if(age < matureAge){
            this.isBaby = true;
            this.displayChar = 'b';
        }else{
            this.isBaby = false;
            this.displayChar = 'B';
        }
    }

    /**
     * Fruit should disappear and should increase the Brachiosaur’s food level by 5 for each fruit consumed.
     *
     * @return true if the Brachiosaur successfully eats a fruit, else return false
     */
    public boolean eatFruit(Actor actor, Location currentLocation) {
        boolean eaten = false;
        if (currentLocation.getGround().getClass().equals(Tree.class)) {
            Tree tree = (Tree) currentLocation.getGround();

            while(tree.inTree.size() != 0 && this.hitPoints < this.maxHitPoints){
                tree.inTree.remove(0);
                actor.heal(5);
            }
            eaten = true;
        }
        return eaten;
    }

    /**
     * Destroys the Bush the Stegosaur is standing on with a chance of failure.
     *
     * @param currentLocation The location of the Stegosaur.
     */
    public void stomp(Location currentLocation) {
        if(currentLocation.getGround().getClass().equals(Bush.class)){
            if(random.nextBoolean()){
                currentLocation.setGround(new Dirt());
            }
        }
    }

    /**
     * This method runs a suitable behaviour depending on what the Brachiosaur is feeling now.
     * If the Brachiosaur wants to mate, it will have the MatingBehaviour, else if it is thirsty, it will
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

        stomp(map.locationOf(this));

        // check if dinosaur can breed
        if (this.hitPoints > 70 && this.inventory.size()==0 && this.age > 50) {
            System.out.println("Brachiosaur at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is healthy enough to breed");
            action = behaviours[2].getAction(this, map);
        }

        if (action != null) {
            return action;
        }

        // check if dinosaur is thirsty
        if (this.isThirsty()){
            System.out.println("Brachiosaur at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is thirsty");
            action = behaviours[3].getAction(this, map);
        }

        if (action != null) {
            return action;
        }

        // check if dinosaur is hungry
        else if (this.hitPoints < 90) {
            System.out.println("Brachiosaur at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is hungry");
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
