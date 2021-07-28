package game.actions;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;
import game.PortableItem;
import game.actors.Allosaur;
import game.actors.Dinosaur;
import game.actors.Pterodactyl;
import game.items.Corpse;

/**
 * Special Action for attacking other Actors.
 *
 * @see edu.monash.fit2099.engine.Action
 * @see game.actors.Dinosaur
 * @see game.actors.Player
 */
public class AttackAction extends Action {

	/**
	 * The Actor that is to be attacked
	 */
	protected Actor target;
	/**
	 * Random number generator
	 */
	protected Random rand = new Random();

	/**
	 * Constructor.
	 * 
	 * @param target the Actor to attack
	 */
	public AttackAction(Actor target) {
		this.target = target;
	}

	/**
	 * Perform the Action.
	 *
	 * @param actor The actor performing the action.
	 * @param map The map the actor is on.
	 * @return a description of what happened that can be displayed to the user.
	 */
	@Override
	public String execute(Actor actor, GameMap map) {

		Weapon weapon = actor.getWeapon();
		String targetSpecies = ((Dinosaur)target).getSpecies();

		if (rand.nextFloat()<0.75) {
			return actor + " misses " + target + ".";
		}

		String result;
		int damage = weapon.damage();
		result = actor + " " + weapon.verb() + " " + target + " for " + damage + " damage.";

		target.hurt(damage);
		if(Allosaur.class.isAssignableFrom(actor.getClass())) {
			if(targetSpecies.equals("Pterodactyl")){
				result = actor + " kills Pterodactyl at ("+map.locationOf(target).x()+","+map.locationOf(target).y()+")";
				actor.heal(((Allosaur) actor).getMaxHitPoints());
				System.out.println(actor+" heals to full.");
				target.hurt(((Pterodactyl) target).getHitPoints());
			}else {
				actor.heal(damage);
				((Allosaur) actor).addCooldown(target);
			}
		}

		if (!target.isConscious()) {
			Corpse corpse = new Corpse(targetSpecies);
			map.locationOf(target).addItem(corpse);

			Actions dropActions = new Actions();
			for (Item item : target.getInventory())
				dropActions.add(item.getDropAction());
			for (Action drop : dropActions)
				drop.execute(target, map);
			map.removeActor(target);

			result += System.lineSeparator() + target + " is killed.";
		}

		return result;
	}

	/**
	 * Returns a descriptive string
	 * @param actor The actor performing the action.
	 * @return the text we put on the menu
	 */
	@Override
	public String menuDescription(Actor actor) {
		return actor + " attacks " + target;
	}
}
