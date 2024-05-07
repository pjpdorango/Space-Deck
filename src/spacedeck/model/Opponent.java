/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import spacedeck.exceptions.FullFieldException;
import spacedeck.exceptions.InsufficientFuelException;

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
    
    public ArrayList<OpponentMove> decideMoves(Character player) {
		ArrayList<OpponentMove> moveset = new ArrayList<>();
        // TODO Opponent AI
        // isn't actually just an attack method, but a "move" method
		
		if (difficulty == AILevel.ADVANCED) {
			// ADVANCED
			// if the opponent has no more cards in playing field left: guarantee deploy a card [DONE]
			// choose randomly from cards that are available, with the weight depending on the attack of the card [DONE]
			// once you pick a card to draw, draw it [DONE]
			// if insuff. fuel and no more cards left: gg skip turn [DONE]
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
			if (hasEmptyField()) {
				try {
					moveset.add(deployWeightedCard());
				} catch (FullFieldException e) {
					System.out.println("[ERROR] " + e.getMessage());
				} catch (InsufficientFuelException e) {
					// If not enough fuel for any cards, just draw a card man
					if (!hasDrawnCard) {
						moveset.add(new OpponentMove(OpponentMove.MoveType.DRAW));
						hasDrawnCard = true;
					}
				}
			}

			else if (!hasFullField()) {
				try {
					OpponentMove move = deployWeightedCard();
					move.setDeployTarget(getWeightedAvailableSlot(player));
					moveset.add(move);
				} catch (FullFieldException e) {
					System.out.println("[ERROR] " + e.getMessage());
				} catch (InsufficientFuelException e) {
					System.out.println("[ERROR] " + e.getMessage());
				}
			}
			// If the field is empty, definitely place a card in the field
		} else if (difficulty == AILevel.RANDOM) {
			Random rand = new Random();
			// 1/2 chance to draw card
			if (rand.nextInt(2) == 0) {
				moveset.add(new OpponentMove(OpponentMove.MoveType.DRAW));
			}
			
			// 1/2 chance to deploy a card
			if (rand.nextInt(2) == 0 && !hasFullField()) {
				OpponentMove move = new OpponentMove(OpponentMove.MoveType.DEPLOY_CARD);
				try {
					move.setDeployCard((Card) deck.get(rand.nextInt(deck.size())));
					move.setDeployTarget(getRandomAvailableSlot());
				} catch (FullFieldException e) {
					System.out.println(e.getMessage());
				}
				moveset.add(move);
			}

			// 1/2 chance to attack with any of avialable playing cards
			for (int i = 0; i < playingCards.length; i++) {
				Card c = playingCards[i];
				if (rand.nextInt(2) == 0 && c != null) {
					OpponentMove move = new OpponentMove(OpponentMove.MoveType.ATTACK);
					move.setAttacker(i);
					moveset.add(move);
				}
			}
		} else if (difficulty == AILevel.EASY) {
			if (!deckHasCards()) {
				OpponentMove move = new OpponentMove(OpponentMove.MoveType.DRAW);
				moveset.add(move);
			} else {
				try {
					moveset.add(deployWeightedCard());
				} catch (FullFieldException e) {
					System.out.println("[ERROR] " + e.getMessage());
					OpponentMove move = new OpponentMove(OpponentMove.MoveType.DRAW);
					moveset.add(move);
				} catch (InsufficientFuelException e) {
					System.out.println("[ERROR] " + e.getMessage());
					moveset.add(new OpponentMove(OpponentMove.MoveType.SKIP));
				}
			}
		}

		if (moveset.isEmpty()) {
			moveset.add(new OpponentMove(OpponentMove.MoveType.SKIP));
		}
		return moveset;
    }

	/**
	 * Deploys a card depending on the weight (attack, health, cost)
	 * @return Corresponding move of deploying a card depending on the weight (Card has been chosen, slot has been randomized)
	 */
	private OpponentMove deployWeightedCard() throws FullFieldException, InsufficientFuelException {
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
		
		// This either means the deck was full of gears, or the current cards
		// have not enough fuel. Since we already checked if there are no cards in
		// the deck, there must be not enough fuel.
		if (weightSum == 0) {
			throw new InsufficientFuelException("No cards exist within the opponent deck with sufficient fuel. [deployWeightedCard()]");
		}

		while (!(randomDeckCard instanceof Card) || randomDeckCard.getCost() >= getFuel()) {
			int weightedIndex = rand.nextInt(weightSum);
			int upperBound = 0;
			for (int i = 0; i < weights.length; i++) {
				upperBound += weights[i];
				if (weightedIndex < upperBound) {
					randomDeckCard = deck.get(i);
					break;
				}
				if (i == weights.length - 1) {
					randomDeckCard = deck.get(i);
				}
			}
		}

		OpponentMove move = new OpponentMove(OpponentMove.MoveType.DEPLOY_CARD);
		move.setDeployCard((Card) randomDeckCard);
		move.setDeployTarget(getRandomAvailableSlot());
		return move;
	}

	private int getRandomAvailableSlot() throws FullFieldException { 
		int[] availableSlots = new int[playingCards.length];
		int nextSlot = 0;
		for (int i = 0; i < playingCards.length; i++) {
			if (playingCards[i] == null) {
				availableSlots[nextSlot] = i;
				nextSlot++;
			}
		}
		if (nextSlot == 0) {
			throw new FullFieldException("The opponent's field is already full, but the AI still tried to place a card.");
		}

		Random rand = new Random();
		return availableSlots[rand.nextInt(nextSlot)];
	}

	private int getWeightedAvailableSlot(Character player) throws FullFieldException {
		HashMap<Integer, Integer> weights = new HashMap<>();
		int maxWeight = 0;
		for (int i = 0; i < playingCards.length; i++) {
			if (playingCards[i] == null) {
				// Check if the opposing side has a card there
				// weight based on attack of that card plus its health
				int runningWeight = 1;
				Card[] playingField = player.getPlayingField();
				if (playingField[i] != null) {
					Card card = playingField[i];
					runningWeight += card.getAttack() + card.getHealth();
					System.out.println(runningWeight);
					ArrayList<Gear> gears = card.getGears();
					for (Gear gear : gears) {
						runningWeight += gear.getAttackFx() + gear.getHealthFx();
						System.out.println(runningWeight);
					}
				}
					System.out.println(runningWeight);

				weights.put(i, runningWeight);
				maxWeight += runningWeight;
			}
		}

		if (weights.isEmpty()) {
			throw new FullFieldException("The opponent's field is already full, but the AI still tried to place a card. [getWeightedAvailableSlot()]");
		}

		Random rand = new Random();
		int decision = rand.nextInt(maxWeight + 1);
		int upperBound = 0;
		for (int key : weights.keySet()) {
			upperBound += weights.get(key);
			if (decision < upperBound) {
				return key;
			}
		}

		return -1;
	}
	
	private boolean hasEmptyField() {
		boolean hasEmptyField = true;
		for (Card c : playingCards) {
			if (c != null) hasEmptyField = false;
		}

		return hasEmptyField;
	}

	private boolean hasFullField() {
		boolean hasFullField = true;
		for (Card c : playingCards) {
			if (c == null) hasFullField = false;
		}

		return hasFullField;
	}
	
	/**
	 * Function that determines whether the opponent deck has any Cards.
	 * Gears do not count towards Cards. This is used for when the AI needs to
	 * deploy a card but must first see if any are available.
	 * 
	 * @return Whether or not the current opponent deck has any Cards
	 */
	private boolean deckHasCards() {
		// Base case
		if (deck.isEmpty()) return false;

		boolean hasCards = false;
		
		for (Deckable d : deck) {
			if (d instanceof Card) {
				hasCards = true;
				break;
			}
		}

		return hasCards;
	}
    
}
