package spacedeck.model;

import java.util.ArrayList;
import spacedeck.exceptions.CardNotActiveException;
import spacedeck.exceptions.CardNotInDeckException;

public class Player extends Character {
    private ArrayList<Card> collection = new ArrayList<>();
	private ArrayList<Item> inventory = new ArrayList<>();
    
    public Player(String n, int f, int a) {
        super(n, f, a);
    }
    
    public void attackWithCard(Card c, Character ch) throws CardNotInDeckException, CardNotActiveException {
        if (!c.getActivity()) {
            throw new CardNotActiveException(c.getName() + " is inactive and cannot be used.");
        }
        
        if (!deck.contains(c)) {
            throw new CardNotInDeckException(c.getName() + " is not in the current deck.");
        }
        
        c.attack(ch);
    }
    
    public void attackWithCard(Card c1, Card c2) throws CardNotInDeckException, CardNotActiveException {
        if (!c1.getActivity()) {
            throw new CardNotActiveException(c1.getName() + " is inactive and cannot be used.");
        }
        
        if (!deck.contains(c1)) {
            throw new CardNotInDeckException(c1.getName() + " is not in the current deck.");
        }
        
        c1.attack(c2);
    }
    
    public void addToCollection(Card c) {
        collection.add(c);
    }
    
    public void removeFromCollection(Card c) {
        collection.remove(c);
    }
    
    public ArrayList<Card> getCollection() {
        return this.collection;
    }

	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public void addToInventory(Item item) {
		for (Item i : inventory) {
			if (i.getName().equals(item.getName())) {
				i.setAmount(i.getAmount() + item.getAmount());
				return;
			}
		}

		inventory.add(item);
	}
}
