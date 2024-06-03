package spacedeck.model;

import java.util.ArrayList;
import spacedeck.exceptions.CardNotActiveException;
import spacedeck.exceptions.CardNotInDeckException;

/**
 * Subclass of the Character class. Represents the actual player.
 * 
 * @author pj
 */
public class Player extends Character {
	/**
	 * The player's entire collection of cards at their disposal.
	 */
    private ArrayList<Card> collection = new ArrayList<>();
	/**
	 * The player's inventory.
	 */
	private ArrayList<Item> inventory = new ArrayList<>();
    
	/**
	 * The constructor for the player class.
	 * 
	 * @param n The player's name.
	 * @param f The player's maximum amount of fuel.
	 * @param a The player's attack.
	 */
    public Player(String n, int f, int a) {
        super(n, f, a);
    }
    
	/**
	 * Attacks a character with a card.
	 * 
	 * @param c Card to attack with
	 * @param ch Character to attack
	 * @throws CardNotInDeckException
	 */
    public void attackWithCard(Card c, Character ch) throws CardNotInDeckException {
        if (!deck.contains(c)) {
            throw new CardNotInDeckException(c.getName() + " is not in the current deck.");
        }
        
        c.attack(ch);
    }
    
	/**
	 * Attacks a card with own card.
	 * 
	 * @param c1 Own card to attack with.
	 * @param c2 Card to attack.
	 * @throws CardNotInDeckException
	 */
    public void attackWithCard(Card c1, Card c2) throws CardNotInDeckException {
        if (!deck.contains(c1)) {
            throw new CardNotInDeckException(c1.getName() + " is not in the current deck.");
        }
        
        c1.attack(c2);
    }
    
	/**
	 * Adds a card to the Player's collection of cards.
	 * 
	 * @param c Card to add.
	 */
    public void addToCollection(Card c) {
        collection.add(c);
    }
    
	/**
	 * Removes a card from the Player's collection of cards.
	 * 
	 * @param c Card to remove.
	 */
    public void removeFromCollection(Card c) {
        collection.remove(c);
    }

	/**
	 * Adds an item to the Player's current inventory. If the Player already has that item, just add the quantity to the already existing item in the Player's inventory.
	 * 
	 * @param item Item to add to the player's inventory.
	 */
	public void addToInventory(Item item) {
		for (Item i : inventory) {
			if (i.getName().equals(item.getName())) {
				i.setAmount(i.getAmount() + item.getAmount());
				return;
			}
		}

		inventory.add(item);
	}
    
    public ArrayList<Card> getCollection() {
        return this.collection;
    }

	public ArrayList<Item> getInventory() {
		return inventory;
	}
}
