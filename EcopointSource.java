package game;

/**
 * Interface for Ecopoint Sources.
 *
 * Ecopoint sources are objects that can generate ecopoints for the player.
 */
public interface EcopointSource {
    /**
     * Give player x amount of ecopoints, where x is initialized in the class implementing the interface.
     */
    void givePoints();
}
