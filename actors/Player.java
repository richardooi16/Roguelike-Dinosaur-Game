package game.actors;

import edu.monash.fit2099.engine.*;
import game.actions.BuyAction;
import game.actions.FeedAction;
import game.actions.HarvestAction;
import game.actions.QuitAction;
import game.ground.Bush;
import game.ground.Lake;
import game.ground.Tree;
import game.ground.VendingMachine;

import java.util.Random;

/**
 * Class representing the Player.
 */
public class Player extends Actor {

	private Menu menu = new Menu();
	public static int ecopoints = 0;
	public int turns = 0;
	private Random rand = new Random();
	private int maxMoves;
	private int ecopointsGoal;
	private int gameType;

	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
	}

	/**
	 * Select and return an action to perform on the current turn. If there is a Bush or Tree nearby, the player
	 * can harvest fruits from it. If there is a Vending Machine nearby, the player can buy items from it. If there
	 * is a Dinosaur nearby, the player can feed the Dinosaur with a suitable item from the player's inventory.
	 *
	 * @param actions    collection of possible Actions for this Actor
	 * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
	 * @param map        the map containing the Actor
	 * @param display    the I/O object to which messages may be written
	 * @return the Action to be performed
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		turns += 1;
		System.out.println("turns: " + turns);

		if (this.turns > maxMoves) {
			return new QuitAction();
		}

		rain(map);

		System.out.println(this.name + "'s ecopoints: " + ecopoints);
		System.out.println(this.hitPoints);

		if (map.locationOf(this).getGround().getClass().equals(Tree.class)
				||map.locationOf(this).getGround().getClass().equals(Bush.class)) {
			actions.add(new HarvestAction());
		}

		if (map.locationOf(this).getGround().getClass().equals(VendingMachine.class)) {
			actions.add(new BuyAction());
		}

		addFeedAction(actions,this, map);

		actions.add(new QuitAction());

		// Handle multi-turn Actions
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();
		return menu.showMenu(this, actions, display);
	}

	/**
	 * Causes rain to fall onto the game map with a chance of failure.
	 *
	 * @param map the current game map
	 */
	private void rain(GameMap map) {
		if (turns%2 == 0) {	// change rain turns here
			if (rand.nextFloat()<0.2) {
				int newWater = (int)(20 * (rand.nextFloat() * (0.6 - 0.1) + 0.1));
				addWater(newWater, map);
				System.out.println("Rain success");
			}
			else{
				System.out.println("Rain failed");
			}
		}
	}

	/**
	 * Adds water to lakes and dinosaurs around the map.
	 *
	 * @param newWater water to be added
	 * @param map the current game map
	 */
	private void addWater(int newWater, GameMap map) {
		for (int x : map.getXRange()) {
			for (int y : map.getYRange()) {
				if (map.at(x,y).getGround().getDisplayChar() == '~') {
					Lake lake = (Lake) map.at(x,y).getGround();
					lake.setSips(lake.getSips()+newWater);
//					System.out.println(lake.getSips());
				}
				else if (map.at(x,y).containsAnActor() && map.at(x,y).getActor().getDisplayChar() != '@') {
					Dinosaur dinosaur = (Dinosaur) map.at(x,y).getActor();
					dinosaur.waterLevel+=newWater;
				}
			}
		}
	}

	/**
	 * Adds a feed action to actions if there is a dinosaur nearby.
	 * @param actions The actions list of player
	 * @param player The player
	 * @param map The map of the game
	 */
	private void addFeedAction(Actions actions, Actor player, GameMap map) {
		Location currentLocale = map.locationOf(player);
		int[] xRange;
		int[] yRange;
		int xPlus = 0,yPlus = 0,xMinus = 0,yMinus = 0;
		for(Exit exit : currentLocale.getExits()){
			if (exit.getDestination().x() == currentLocale.x()+1){
				xPlus = 1;
			}if (exit.getDestination().y() == currentLocale.y()+1){
				yPlus = 1;
			}if (exit.getDestination().x() == currentLocale.x()-1){
				xMinus = -1;
			}if (exit.getDestination().y() == currentLocale.y()-1){
				yMinus = -1;
			}
		}
		xRange = new int[]{xMinus, 0, xPlus};
		yRange = new int[]{yMinus, 0, yPlus};
		for (int x: xRange) {
			for (int y: yRange) {
				if (x==0 && y==0) {
					continue;
				} else if (map.at(currentLocale.x()+x, currentLocale.y()+y).containsAnActor()) {
					actions.add(new FeedAction(map.getActorAt(map.at(currentLocale.x()+x, currentLocale.y()+y))));
				}
			}
		}

	}

	public void setMaxMoves(int maxMoves) {
		this.maxMoves = maxMoves;
	}

	public void setEcopointsGoal(int ecopointsGoal) {
		this.ecopointsGoal = ecopointsGoal;
	}

	public int getEcopointsGoal() {
		return ecopointsGoal;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public int getGameType() {
		return gameType;
	}
}
