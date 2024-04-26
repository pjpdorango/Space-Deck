/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

import java.util.ArrayList;
import java.util.Random;
import spacedeck.exceptions.FullFieldException;

/**
 *
 * @author pj
 */
public class Opponent extends Character {
    AILevel difficulty = AILevel.EASY;
    
    public Opponent(String n, int f, int a, AILevel diff) {
        super(n, f, a);
        difficulty = diff;
    }
    
    public ArrayList<OpponentMove> decideMoves() {
		ArrayList<OpponentMove> moveset = new ArrayList<>();
        // TODO Opponent AI
        // isn't actually just an attack method, but a "move" method
        // ADVANCED
        // if the opponent has no more cards left: guarantee draw a card
        // choose randomly from cards that are available, with the weight depending on the attack of the card
        // once you pick a card to draw, draw it
        // if insuff. fuel and no more cards left: gg skip turn
        // if still has cards:
        // if has gear in deck:
        // decide whether or not to use a gear
        // evaluate based on current fuel over max fuel (weight chance)
        // if so, pick a gear to use (strongest one fuel allows)
        // pick card to use it on (current strongest card)
        // evaluate the total hp left of the playing cards
        // decide whether or not to pick a card with the weight depending on the total hp
        // if already at max cards dont pick anymore
        // same principles of picking card to draw as before
        // attack opponent

		boolean hasDrawnCard = false;

        // take each playing card and attack the opponents side
		// If deck is empty, MUST draw card from stack
		// Does not need to return after this
		// But now card has already drawn card
		if (deck.isEmpty()) {
			moveset.add(new OpponentMove(OpponentMove.MoveType.DRAW));
			hasDrawnCard = true;
		}
		
		// If the field is empty, definitely place a card in the field 
		if (!hasEmptyField()) {
			try {
				moveset.add(deployWeightedCard());
			} catch (FullFieldException e) {
				System.out.println(e.getMessage());
			}
		}

		// If the field is empty, definitely place a card in the field
		return moveset;
    }

	/**
	 * Deploys a card depending on the weight (attack, health, cost)
	 * @return Corresponding move of deploying a card depending on the weight (Card has been chosen, slot has been randomized)
	 */
	private OpponentMove deployWeightedCard() throws FullFieldException {
		Random rand = new Random();
		Deckable randomDeckCard = null;

		// Setting up the weights
		int[] weights = new int[deck.size()];
		int weightSum = 0;
		for (int i = 0; i < deck.size(); i++) {
			Deckable d = deck.get(i);
			if (d instanceof Gear || d.getCost() >= getFuel()) { 
				weights[i] = 0;
				break;
			} 
			Card c = (Card) d;
			weights[i] = (c.getAttack() + c.getHealth()) / c.getCost();
			weightSum += weights[i];
		}
		
		while (!(randomDeckCard instanceof Card) || randomDeckCard.getCost() < getFuel()) {
			int weightedIndex = rand.nextInt(weightSum);
			int upperBound = 0;
			for (int i = 0; i < weights.length; i++) {
				upperBound += weights[i];
				if (weightedIndex < upperBound) {
					randomDeckCard = deck.get(i);
					break;
				}
			}
		}

		OpponentMove move = new OpponentMove(OpponentMove.MoveType.DEPLOY_CARD);
		move.setDeployCard(randomDeckCard);
		move.setDeployTarget(getRandomAvailableSlot());
		return move;
	}

	private int getRandomAvailableSlot() throws FullFieldException { 
		int[] availableSlots = new int[playingCards.length];
		int nextSlot = 0;
		for (int i = 0; i < playingCards.length; i++) {
			if (playingCards[i] != null) {
				availableSlots[nextSlot] = i;
				nextSlot++;
			}
		}
		if (nextSlot == 0) {
			throw new FullFieldException("The opponent's field is already full, but the AI still tried to place a card.");
		}

		Random rand = new Random();
		return availableSlots[rand.nextInt(availableSlots.length)];
	}
	
	private boolean hasEmptyField() {
		boolean hasEmptyField = true;
		for (Card c : playingCards) {
			if (c != null) hasEmptyField = false;
		}

		return hasEmptyField;
	}

    
}
