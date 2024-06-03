package spacedeck.model;

import java.util.ArrayList;
import spacedeck.exceptions.CardAlreadyActiveException;
import spacedeck.exceptions.CardNotInDeckException;
import spacedeck.exceptions.FullDeckException;
import spacedeck.exceptions.InsufficientFuelException;

public abstract class Character implements Attackable {
    // Constants
	/**
	 * The maximum number of cards in a Character's deck. Default is 6.
	 */
    private static final int MAXCARDS = 6;
	/**
	 * The initial fuel a Character starts a match with. Default is 10.
	 */
    private static final int INITIALFUEL = 10; // All characters start with 10 fuel.
	/**
	 * The maximum number of playing cards in a Character's field. Default is 3.
	 */
    private static final int MAXPLAYINGCARDS = 3;
    
    // Character properties
	/**
	 * The Character's name.
	 */
    private String name;
	/**
	 * The Character's current amount of fuel.
	 */
    private int fuel;
	/**
	 * The maximum fuel a Character can have.
	 */
	private int maxFuel;
	/**
	 * The Character's attack.
	 */
	private int attack;
	/**
	 * The Character's deck. These are the cards not yet on the playing field and are to be deployed, but are options for the Character.
	 */
    protected ArrayList<Deckable> deck = new ArrayList<Deckable>();
	/**
	 * The Character's playing field, where all of the cards lie.
	 */
    protected Card[] playingCards;
    
	/**
	 * Constructor for the Character class.
	 * Sets the initial amount of fuel to {@link INITIALFUEL}.
	 * 
	 * @param n Name of the character.
	 * @param f Maximum fuel of the character.
	 * @param a Attack of the character.
	 */
    public Character(String n, int f, int a) {
        this.name = n;
        this.maxFuel = f;
        this.fuel = INITIALFUEL;
        this.attack = a;
        
        playingCards = new Card[MAXPLAYINGCARDS];
    }
    
	/**
	 * Adds a card to the player's deck.
	 * 
	 * @param c The card to be added.
	 * @throws FullDeckException If the Character has more than {@link MAXCARDS} cards in their deck.
	 */
    public void addCard(Deckable c) throws FullDeckException {
		if (c == null) { return; }

        if (deck.size() >= MAXCARDS) {
            throw new FullDeckException("Your deck cannot have more than " + MAXCARDS + " cards.");
        }
        
        deck.add(c);
    }
    
	/**
	 * Draws, or deploys, a card into a slot in the playing field.
	 * 
	 * @param c The card to be deployed.
	 * @param slot The slot where the card will be deployed.
	 * @throws InsufficientFuelException
	 * @throws CardAlreadyActiveException 
	 */
    public void drawCard(Card c, int slot) throws InsufficientFuelException, CardAlreadyActiveException {
        if (fuel <= c.getCost()) {
            throw new InsufficientFuelException(
                    "Your fuel must be greater than " + c.getCost() + " to draw a card!"
            );
        }
        
        if (!deck.contains(c)) {
            throw new CardAlreadyActiveException(
                    "The card " + c.getName() + " must be a card that has not been drawn yet!"
            );
        }
        
        fuel -= c.getCost();
        deck.remove(c);
        playingCards[slot] = c;
    }
    
	/**
	 * Deletes, or undraws, a card from the playing field.
	 * 
	 * @param slot The slot whose card will be removed.
	 * @throws CardNotInDeckException
	 */
    public void undrawCard(int slot) throws CardNotInDeckException {
        // If the slot specified is empty, throw a CardNotInDeckException
        if (playingCards[slot] == null) {
            throw new CardNotInDeckException("The card is not in the current deck.");
        }
        
        // Otherwise,
        playingCards[slot] = null;
    }
    
	/**
	 * Attacks a card.
	 * 
	 * @param c The Card to be attacked.
	 */
    @Override
    public void attack(Card c) {
        // Just in case we need to scale the dmg
        int dmg = attack;
        
        c.takeDamage(dmg);
    }
    
	/**
	 * Attacks a character.
	 * 
	 * @param ch The Character to be attacked.
	 */
    @Override
    public void attack(Character ch) {
        // Just in case we need to scale the dmg
        int dmg = attack;
        
        ch.takeDamage(dmg);
    } 

	/**
	 * Takes {@code dmg} amount of damage to the fuel count.
	 * 
	 * @param dmg Amount of damage to be taken.
	 */
    @Override
    public void takeDamage(int dmg) {
        fuel -= dmg;
    }

	/**
	 * Resets all of the varying attributes of a character per try.
	 */
    public void reset() {
        fuel = INITIALFUEL;
		for (int i = 0; i < playingCards.length; i++) {
			playingCards[i] = null;
		}
    }
    
    // GETTERS & SETTERS

    public String getName() {
        return name;
    }
    
    public int getFuel() {
        return fuel;
    }
    
	/**
	 * Add {@code increment} amount of fuel to the Character.
	 * 
	 * @param increment Amount of fuel to add.
	 */
    public void addFuel(int increment) {
        fuel += increment;
        
        if (fuel > maxFuel) {
            fuel = maxFuel;
        }
    }
    
    public int getAttack() {
        return attack;
    }
    
    public ArrayList<Deckable> getDeck() {
        return deck;
    }

	public Card[] getPlayingField() {
		return playingCards;
	}

	public int getMaxFuel() {
		return maxFuel;
	}
}
