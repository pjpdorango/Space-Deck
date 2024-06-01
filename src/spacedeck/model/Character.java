package spacedeck.model;

import java.util.ArrayList;
import spacedeck.exceptions.CardAlreadyActiveException;
import spacedeck.exceptions.CardNotInDeckException;
import spacedeck.exceptions.FullDeckException;
import spacedeck.exceptions.InsufficientFuelException;

public abstract class Character implements Attackable {
    // Constants
    private static final int MAXCARDS = 6;
    private static final int INITIALFUEL = 10; // All characters start with 10 fuel.
    private static final int MAXPLAYINGCARDS = 3;
    
    // Character properties
    private String name;
    private int fuel, maxFuel, attack;
    protected ArrayList<Deckable> deck = new ArrayList<Deckable>();
    protected Card[] playingCards;
    
    public Character(String n, int f, int a) {
        this.name = n;
        this.maxFuel = f;
        this.fuel = INITIALFUEL;
        this.attack = a;
        
        playingCards = new Card[MAXPLAYINGCARDS];
    }
    
    public void addCard(Deckable c) throws FullDeckException {
		if (c == null) {
			return;
		}
        if (deck.size() >= MAXCARDS) {
            throw new FullDeckException("Your deck cannot have more than " + MAXCARDS + " cards.");
        }
        
        deck.add(c);
    }
    
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
        c.setActivity(true);
    }
    
    public void undrawCard(int slot) throws CardNotInDeckException {
        // If the slot specified is empty, throw a CardNotInDeckException
        if (playingCards[slot] == null) {
            throw new CardNotInDeckException("The card is not in the current deck.");
        }
        
        // Otherwise,
        playingCards[slot] = null;
    }
    
    // COMMENTED OUT: drawCard() has the same functionality. I think. Yeah no it does
//    public void replaceCard(Card oldCard, Card newCard) 
//            throws InsufficientFuelException, CardNotActiveException, CardAlreadyActiveException {
//        // If the fuel is insufficient
//        if (fuel <= newCard.getCost()) {
//            throw new InsufficientFuelException(
//                    "Your fuel must be greater than " + newCard.getCost() + " to replace a card!"
//            );
//        }
//        
//        // If the card to be replaced is not already in the playingCards
//        if (!playingCards.contains(oldCard)) {
//            throw new CardNotActiveException(
//                    "The card " + oldCard.getName() + " must be a card that has been drawn!"
//            );
//        }
//        
//        if (playingCards.contains(newCard)) {
//            throw new CardAlreadyActiveException(
//                    "The card " + newCard.getName() + " must be a card that has not been drawn yet!"
//            );
//        }
//        
//        fuel -= 2;
//        
//        oldCard.setActivity(false);
//        newCard.setActivity(true);
//    }
    
    @Override
    public void attack(Card c) {
        // Just in case we need to scale the dmg
        int dmg = attack;
        
        c.takeDamage(dmg);
    }
    
    @Override
    public void attack(Character ch) {
        // Just in case we need to scale the dmg
        int dmg = attack;
        
        ch.takeDamage(dmg);
    }
    
    @Override
    public void takeDamage(int dmg) {
        fuel -= dmg;
    }
    // GETTERS & SETTERS

    public String getName() {
        return name;
    }
    
    public int getFuel() {
        return fuel;
    }
    
    public void addFuel(int increment) {
        fuel += increment;
        
        if (fuel > maxFuel) {
            fuel = maxFuel;
        }
    }
    
    public int getAttack() {
        return attack;
    }
    
    public void reset() {
        fuel = INITIALFUEL;
		for (int i = 0; i < playingCards.length; i++) {
			playingCards[i] = null;
		}
		deck.clear();
    }
    
    public ArrayList<Deckable> getDeck() {
        return deck;
    }

	public Card[] getPlayingField() {
		return playingCards;
	}
}
