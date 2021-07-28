package game.ground;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;
import game.actors.Pterodactyl;

import java.util.Random;

public class Lake extends Ground {

    private int sips = 25;
    private int fish = 5;
    private int MAX_FISH = 25;
    private Random rand = new Random();

    /**
     * Constructor.
     */
    public Lake() {
        super('~');
    }

    /**
     * Lake can also experience the joy of time.
     * @param location The location of the Lake
     */
    @Override
    public void tick(Location location) {
        super.tick(location);
        addFish();
//        System.out.println(this.sips);
//        System.out.println(this.fish);
    }

    /**
     * Adds new fish into the lake.
     *
     */
    private void addFish() {
        if (rand.nextFloat()<0.6 && this.fish < MAX_FISH) {
            this.fish += 1;
//            System.out.println("new FIsh");
        }
    }

    /**
     * Returns boolean showing whether an actor can enter this ground.
     *
     * @param actor the Actor to check
     * @return true if actor is a Pterodactyl else false
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        if (actor.getDisplayChar() == 'P') {
            if(((Pterodactyl) actor).isFlying()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter function for sips
     *
     * @return sips of the lake
     */
    public int getSips() {
        return sips;
    }

    /**
     * Setter function for sips
     *
     * @param sips new sips number for lake
     */
    public void setSips(int sips) {
        this.sips = sips;
    }

    /**
     * Function to remove eaten fish from lake
     *
     * @param fishAmt number of fish to remove
     *
     * @return true if number of fish in lake is more than or equal to fishAmt
     */
    public boolean eatFish(int fishAmt){
        boolean enoughFish = false;
        if (fish-fishAmt >= 0){
            enoughFish = true;
            fish -= fishAmt;
        }
        return enoughFish;
    }
}
