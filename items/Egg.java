package game.items;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.Location;
import game.EcopointSource;
import game.PortableItem;
import game.actors.*;
import game.ground.Tree;

/**
 * A class for Egg item. Eggs can be created by either Dinosaurs breeding or buying from a
 * Vending Machine. Eggs that are created by Dinosaurs will have a longer hatching time
 * than buying from Vending Machine. When the Dinosaur hatches, a Dinosaur will take its
 * place. When an Egg hatches, ecopoints are given depending on the species born.
 *
 * @see game.PortableItem
 * @see Dinosaur
 * @see Player
 */
public class Egg extends PortableItem implements EcopointSource {

    private int timeToHatch;
    private String species;

    /**
     * Constructor for egg.
     *
     * @param type the species of dinosaur
     * @param forActor actor receiving the egg
     */
    public Egg(String type, Class forActor) {
        super("Egg", 'E');

        if(type.equals("Stegosaur")) {
            species = "Stegosaur";
            this.timeToHatch = 10;
            // this.timeToHatch = 10; // to test hatching
            if (forActor.equals(Player.class)) {
                this.timeToHatch = 30;
            }
        }
        else if(type.equals("Brachiosaur")){
            species = "Brachiosaur";
            this.timeToHatch = 80;
            if (forActor.equals(Player.class)) {
                this.timeToHatch = 50;
            }
        }
        else if(type.equals("Allosaur")){
            species = "Allosaur";
            this.timeToHatch = 70;
            if (forActor.equals(Player.class)) {
                this.timeToHatch = 50;
            }
        }
        else if(type.equals("Pterodactyl")){
            species = "Pterodactyl";
            this.timeToHatch = 50;
            if (forActor.equals(Player.class)) {
                this.timeToHatch = 30;
            }
        }
    }

    /**
     * Constructor for egg.
     *
     * @param type the species of dinosaur
     * @param timeToHatch time for egg to hatch
     */
    public Egg(String type, int timeToHatch) {
        super("Egg", 'E');
        this.species = type;
        this.timeToHatch = timeToHatch;
    }

    /**
     * Inform egg on the ground of the passage of time.
     * This method is called once per turn, if the egg rests upon the ground.
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation){
        super.tick(currentLocation);
        if(timeToHatch == 0 ){
            this.hatch(currentLocation, this.species);
        }
        timeToHatch -= 1;
    }

    /**
     * Inform egg of the passage of time when in an inventory.
     *
     * This method is called once per turn, if the Item is being carried.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor){
        super.tick(currentLocation, actor);
        if(actor.getDisplayChar() != '@' && species.equals("Stegosaur") && timeToHatch == 5) { // 30
            actor.removeItemFromInventory(this);
            currentLocation.addItem(new Egg(this.species, 5));
        }
        else if(actor.getDisplayChar() != '@' && species.equals("Brachiosaur") && timeToHatch == 50) {
            actor.removeItemFromInventory(this);
            currentLocation.addItem(new Egg(this.species, 30));
        }
        else if(actor.getDisplayChar() != '@' && species.equals("Allosaur") && timeToHatch == 50) {
            actor.removeItemFromInventory(this);
            currentLocation.addItem(new Egg(this.species, 20));
        }
        else if(actor.getDisplayChar() != '@' && species.equals("Pterodactyl") && timeToHatch == 30
                && currentLocation.getGround().getClass().equals(Tree.class)) {
            actor.removeItemFromInventory(this);
            currentLocation.addItem(new Egg(this.species, 15));
        }
        if(timeToHatch == 0 ){
            this.hatch(currentLocation, this.species, actor);
        }
        timeToHatch -= 1;
    }

    /**
     * Removes the egg from the ground and creates a new Dinosaur depending on the species.
     *
     * @param currentLocation The location of the egg
     * @param species The species of the dinosaur about to be hatched
     */
    public void hatch(Location currentLocation, String species){
        currentLocation.removeItem(this);
        Dinosaur baby = null;
        if(this.species.equals("Stegosaur")){
            baby = new Stegosaur(species, 10,0 );
        }
        else if(this.species.equals("Brachiosaur")){
            baby = new Brachiosaur(species, 10,0 );
        }
        else if(this.species.equals("Allosaur")){
            baby = new Allosaur(species, 20,0 );
        }
        else if(this.species.equals("Pterodactyl")){
            baby = new Pterodactyl(species, 20,0 );
        }
        for (Exit exit : currentLocation.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(baby)) {
                destination.map().addActor(baby, destination);
                break;
            }
        }
        givePoints();
    }

    /**
     * Removes the egg from an inventory and creates a new Dinosaur depending on the species.
     *
     * @param currentLocation The location of the carrier of the egg
     * @param species The species of the dinosaur about to be hatched
     * @param actor The carrier of the egg
     */
    public void hatch(Location currentLocation, String species, Actor actor){
        actor.removeItemFromInventory(this);
        Dinosaur baby = null;
        if(this.species.equals("Stegosaur")){
            baby = new Stegosaur(species, 10,0 );
        }
        else if(this.species.equals("Brachiosaur")){
            baby = new Brachiosaur(species, 10,0 );
        }
        else if(this.species.equals("Allosaur")){
            baby = new Allosaur(species, 20,0 );
        }
        else if(this.species.equals("Pterodactyl")){
            baby = new Pterodactyl(species, 20,0 );
        }
        for (Exit exit : currentLocation.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(baby)) {
                destination.map().addActor(baby, destination);
                break;
            }
        }
        givePoints();
    }

    /**
     * Give player 100 ecopoints if Stegosaur or Pterodactyl hatches, else give 1000 ecopoints
     * if Brachiosaur or Allosaur hatches.
     */
    @Override
    public void givePoints() {
        if(this.species.equals("Stegosaur")){
            Player.ecopoints += 100;
        }
        else if(this.species.equals("Brachiosaur")){
            Player.ecopoints += 1000;
        }
        else if(this.species.equals("Allosaur")){
            Player.ecopoints += 1000;
        }
        else if(this.species.equals("Pterodactyl")){
            Player.ecopoints += 100;
        }
    }
}
