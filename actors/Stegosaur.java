package game.actors;


import edu.monash.fit2099.engine.*;
import game.ground.Bush;
import game.items.Fruit;

import java.util.ArrayList;

/**
 * A herbivorous dinosaur that can eat from bushes and on the ground.
 */
public class Stegosaur extends Dinosaur {
	// 0=Wander, 1=Hungry, 2=Attack, 3=Mate
//	private final Behaviour[] behaviours = new Behaviour[3];

	/**
	 * Constructor.
	 * All Stegosaurs are represented by a 'S' and have 100 max hit points.
	 *
	 * @param name the name of this Stegosaur
	 */
	public Stegosaur(String name) {
		super(name, 'S', 70);
		species = "Stegosaur";
		maxHitPoints = 100;
		matureAge = 30;

//		behaviours[0] = new WanderBehaviour();
//		behaviours[1] = new HungryBehaviour();
//		behaviours[2] = new MatingBehaviour();

		// Dinosaur is constructed with a random age unless specified.
		age = random.nextInt(61);
	}

	/**
	 * Constructor.
	 * All Stegosaurs are represented by a 'S' and have 100 max hit points.
	 *
	 * @param name the name of this Stegosaur
	 * @param gender The gender of this Stegosaur
	 */
	public Stegosaur(String name, String gender) {
		super(name, 'S', 50);
//		super(name, 'S', 5);	// to test unconsciousness
//		super(name, 'S', 100);	// to test mating
		species = "Stegosaur";
		maxHitPoints = 100;
		matureAge = 30;

//		behaviours[0] = new WanderBehaviour();
//		behaviours[1] = new HungryBehaviour();
//		behaviours[2] = new MatingBehaviour();

		// Dinosaur is constructed with a random age unless specified.
//		age = random.nextInt(61);
		age = 40;
		this.gender = gender;
	}

	/**
	 * Constructor.
	 * All Stegosaurs are represented by a 'S' and have 100 max hit points.
	 *
	 * @param name The name of this Stegosaur
	 * @param hitPoints The hitpoints of this Stegosaur
	 * @param age The age of this Stegosaur
	 */
	public Stegosaur(String name, int hitPoints, int age){
		super(name, '*', hitPoints, age);
		species = "Stegosaur";
		maxHitPoints = 100;
		matureAge = 30;

//		behaviours[0] = new WanderBehaviour();
//		behaviours[1] = new HungryBehaviour();
//		behaviours[2] = new MatingBehaviour();

		if(age < matureAge){
			this.isBaby = true;
			this.displayChar = 's';
		}else{
			this.isBaby = false;
			this.displayChar = 'S';
		}
	}

	/**
	 * Fruit should disappear and should increase the stegosaur’s food level by 10.
	 *
	 * @return true if the stegosaur successfully eats a fruit, else return false
	 */
	public boolean eatFruit(Actor actor, Location currentLocation) {
		boolean eaten = false;
		if (currentLocation.getGround().getClass().equals(Bush.class)) {
			Bush bush = (Bush)currentLocation.getGround();
			ArrayList fruits = bush.inBush;
//			System.out.println("Fruits in the bush before: "+fruits.size());
			for (Object fruit : fruits) {
				fruits.remove(fruit);
				actor.heal(10);
				eaten = true;
				break;
			}
//			System.out.println("Fruits in the bush after: "+fruits.size());
		}
		else if(currentLocation.getItems().size() != 0){
			for (Item item : currentLocation.getItems()){
				if (item.getClass().equals(Fruit.class)){
					currentLocation.removeItem(item);
					actor.heal(10);
					eaten = true;
					break;
				}
			}
		}
//		System.out.println(this.hitPoints);
		return eaten;
	}

	/**
	 * This method runs a suitable behaviour depending on what the Stegosaur is feeling now.
	 * If the Stegosaur wants to mate, it will have the MatingBehaviour, else if it is thirsty, it will
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

		System.out.println("Health: "+this.hitPoints);
		System.out.println("Water Level: "+this.waterLevel);

		if (action != null) {
			return action;
		}

		// check if dinosaur can breed
		if (this.hitPoints > 50 && this.inventory.size()==0 && this.age > 30) {
			System.out.println("Stegosaur at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is healthy enough to breed");
			action = behaviours[2].getAction(this, map);
		}

		if (action != null) {
			return action;
		}

		// check if dinosaur is thirsty
		if (this.isThirsty()){
			System.out.println("Stegosaur at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is thirsty");
			action = behaviours[3].getAction(this, map);
		}

		if (action != null) {
			return action;
		}

		// check if dinosaur is hungry
		else if (this.hitPoints < 90) {
			System.out.println("Stegosaur at (" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ") is hungry");
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
