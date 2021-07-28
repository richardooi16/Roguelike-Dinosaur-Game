package game.actions;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.actors.Player;

/**
 * A Quit game action for the player when the player doesn't want to play anymore.
 *
 * @see Action
 * @see Player
 */
public class QuitAction extends Action {
    @Override
    public String execute(Actor actor, GameMap map) {
        Player player = (Player) actor;
        if(player.getGameType() == 1 && Player.ecopoints >= player.getEcopointsGoal()) {
            System.out.println("YOU WIN");
        }
        else if(player.getGameType() == 1 && Player.ecopoints < player.getEcopointsGoal()) {
            System.out.println("YOU LOSE");
        }
        int final_score = Player.ecopoints;
        map.removeActor(player);
        return "Your score: " + final_score;
    }

    @Override
    public String menuDescription(Actor actor) {
        return "Quit the game";
    }
}
