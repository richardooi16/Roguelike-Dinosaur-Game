package game;

import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

import edu.monash.fit2099.engine.*;
import game.actors.*;
import game.ground.*;
import game.items.Waypoint;

/**
 * The main class for the Jurassic World game.
 *
 */
public class Application {

	private static int maxMoves;
	private static int ecoPointsGoal;
	private static boolean retry=false;

	public static void main(String[] args) throws IOException {
		World world;
		GameMap gameMap;
		GameMap gameMap2;

		FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Tree(), new Lake());
		
		List<String> map = Arrays.asList(
		"................................................................................",
		"................................................................................",
		".....#######....................................................................",
		".....#_____#................~~~.................................................",
		".....#_____#...................~................................................",
		".....###.###....................................................................",
		"................................................................................",
		"......................................+++.......................................",
		".......................................++++.....................................",
		"...................................+++++........................................",
		"....................~~~~~~~..........++++++.....................................",
		"..........................~~..........+++.......................................",
		".....................................+++........................................",
		"................................................................................",
		"............+++.................................................................",
		".............+++++..............................................................",
		"...............++........................................+++++..................",
		".............+++....................................++++++++....................",
		"............+++.......................................+++.......................",
		"................................................................................",
		".........................................................................++.....",
		"........................................................................++.++...",
		".........................................................................++++...",
		"..........................................................................++....",
		"................................................................................");

		List<String> map2 = Arrays.asList(
				"................................................................................",
				"..................................~~~~..........................................",
				"...............................~~~~~~~~~........................................",
				".............................~~~~~~~~~~~~~......................................",
				"...............................~~~~~~~~~........................................",
				".................................~~~~~..........................................",
				"......++++++....................................................................",
				".........+++++..................................................................",
				".........++++..................................+++++++++++......................",
				"......++++++++.................................++++++...........................",
				"..........................................++++++++..............................",
				"..................+++++++++..................++++...............................",
				".....................+++++......................................................",
				"........................+++++...................................................",
				"..........................+++...........................+++++++++...............",
				"......................................................++++++....................",
				"................................................................................",
				"..................~~~...........................................................",
				"................~~~~~~~..................................++++++++++.............",
				"..............~~~~~~~~~~~.................................~~~~~~~~++............",
				"................~~~~~~~~..................................~~~~~~~~~~+++.........",
				"..................~~~~......................................~~~~~~+++...........",
				"..............................................................~~+++++...........",
				"..................................................................++++++++++....",
				"................................................................................");

		Player player = new Player("Player", '@', 100);

		world = new World(new Display());
		gameMap = new GameMap(groundFactory, map);
		world.addGameMap(gameMap);

		// Create new gameMap
        gameMap2 = new GameMap(groundFactory, map2);
        world.addGameMap(gameMap2);

        for(int x = 0; x < map.get(0).length(); x++){
			Waypoint borderUp = new Waypoint("border", '^', false);
			borderUp.addAction(new MoveActorAction(gameMap2.at(x, map2.size()-1), "to next Map"));
			gameMap.at(x, 0).addItem(borderUp);

			Waypoint borderDown = new Waypoint("border", 'v', false);
			borderDown.addAction(new MoveActorAction(gameMap.at(x, 0), "to original Map"));
			gameMap2.at(x, map2.size()-1).addItem(borderDown);
		}

		createWorld(world, player, gameMap);

		int gameType = printMenu();
		while (gameType!=3) {

			if(retry){
				world = new World(new Display());
				gameMap = new GameMap(groundFactory, map );
				world.addGameMap(gameMap);

				player = new Player("Player", '@', 100);

				// Create new gameMap
				gameMap2 = new GameMap(groundFactory, map2);
				world.addGameMap(gameMap2);

				for(int x = 0; x < map.get(0).length(); x++){
					Waypoint borderUp = new Waypoint("border", '^', false);
					borderUp.addAction(new MoveActorAction(gameMap2.at(x, map2.size()-1), "to next Map"));
					gameMap.at(x, 0).addItem(borderUp);

					Waypoint borderDown = new Waypoint("border", 'v', false);
					borderDown.addAction(new MoveActorAction(gameMap.at(x, 0), "to original Map"));
					gameMap2.at(x, map2.size()-1).addItem(borderDown);
				}

				createWorld(world, player, gameMap);
			} else {
				retry=true;
			}

			if (gameType == 1) {
				maxMoves = printMovesMenu();
				ecoPointsGoal = printEcopointsMenu();
			} else {
				maxMoves = Integer.MAX_VALUE;
				ecoPointsGoal = Integer.MAX_VALUE;
			}

			player.setMaxMoves(maxMoves);
			player.setEcopointsGoal(ecoPointsGoal);
			player.setGameType(gameType);
			world.run();

			gameType = printMenu();
		}
	}

	/**
	 * Method to add actors, objects and set Grounds to the gamemap.
	 *
	 * @param world The current world the game is on
	 * @param player The player object
	 * @param gameMap The gamemap to place objects
	 */
	private static void createWorld(World world, Player player, GameMap gameMap){
		Player.ecopoints = 0;

		world.addPlayer(player, gameMap.at(9, 4));

		// Place a pair of stegosaurs in the middle of the map
		gameMap.at(30, 12).addActor(new Stegosaur("Stegosaur", "female"));
		gameMap.at(32, 12).addActor(new Stegosaur("Stegosaur", "male"));
//		gameMap.at(29, 11).addActor(new Pterodactyl("Pterodactyl"));

		gameMap.at(10,10).setGround(new VendingMachine());
	}

	/**
	 * Prints start menu and returns the input given by user
	 *
	 * @return user input for game type
	 */
	private static int printMenu(){
		int input = 0;

		while(!(input>=1 && input<=3)) {
			System.out.println("___________________________");
			System.out.println("| Game options            |");
			System.out.println("___________________________");
			System.out.println("1) Challenge Mode");
			System.out.println("2) Sandbox Mode");
			System.out.println("3) Quit the game");
			System.out.println("___________________________");

			Scanner scanner = new Scanner(System.in);
			try {
				System.out.print("Enter Option: ");
				input = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input");
			}
		}

		return input;
	}

	/**
	 * Prints menu for moves amount selection and returns the input given by user
	 *
	 * @return user input for moves amount
	 */
	private static int printMovesMenu(){

		int input = 0;
		while(!(input>=1 && input<=4)) {
			System.out.println("___________________________");
			System.out.println("| Number of max moves     |");
			System.out.println("___________________________");
			System.out.println("1) 5 (for debugging/demonstration)");
			System.out.println("2) 50");
			System.out.println("3) 100");
			System.out.println("4) 200");
			System.out.println("___________________________");

			Scanner scanner = new Scanner(System.in);
			try {
				System.out.print("Enter Option: ");
				input = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input");
			}
		}

		int maxMoves = 0;

		switch (input){
			case 1:
				maxMoves = 5;
				break;

			case 2:
				maxMoves = 50;
				break;

			case 3:
				maxMoves = 100;
				break;

			case 4:
				maxMoves = 200;
				break;
		}

		return maxMoves;
	}

	/**
	 * Prints menu for ecopoints amount selection and returns the input given by user
	 *
	 * @return user input for ecopoints amount
	 */
	private static int printEcopointsMenu() {
		int input = 0;
		while(!(input>=1 && input<=4)) {
			System.out.println("___________________________");
			System.out.println("| Eco points goal         |");
			System.out.println("___________________________");
			System.out.println("1) 100 (for debugging/demonstration)");
			System.out.println("2) 5000");
			System.out.println("3) 10000");
			System.out.println("4) 20000");
			System.out.println("___________________________");

			Scanner scanner = new Scanner(System.in);
			try {
				System.out.print("Enter Option: ");
				input = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input");
				// input = 0
			}
		}

		int ecopointsGoal = 0;

		switch (input){
			case 1:
				ecopointsGoal = 100;
				break;

			case 2:
				ecopointsGoal = 5000;
				break;

			case 3:
				ecopointsGoal = 10000;
				break;

			case 4:
				ecopointsGoal = 20000;
				break;
		}

		return ecopointsGoal;
	}
}
