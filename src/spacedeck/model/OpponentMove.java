/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

/**
 * Helper class to disseminate information on a Move done by the opponent. <br>
 * <b> NOTE: </b>
 * Not all parameters are set from the get-go. Some other attributes must be
 * set depending on the move type:
 * 
 * <ol>
 * 	<li> {@code SKIP} - No attributes needed. </li>
 * 	<li> {@code DRAW} - No attributes needed. </li>
 * 	<li> {@code ATTACK} - Requires {@code attacker} and {@code attackTarget} to be set. </li>
 * 	<li> {@code ATTACK_CHARACTER} - Requires {@code characterAttacker} and {@code characterTarget} to be set. </li>
 * 	<li> {@code DEPLOY_CARD} - Requires {@code deployCard} and {@code deployTarget} to be set. </li>
 * </ol>
 * 
 * @author pj
 */
public class OpponentMove {
	/**
	 * Enum that lists all of the move types. As of June 2, 2024, the move types are:
	 * <ol>
	 * <li><span style="color: #f731dd;"><i>SKIP</i></span></li>
	 * <li><span style="color: #f731dd;"><i>DRAW</i></span></li>
	 * <li><span style="color: #f731dd;"><i>ATTACK</i></span></li>
	 * <li><span style="color: #f731dd;"><i>ATTACK_CHARACTER</i></span></li>
	 * <li><span style="color: #f731dd;"><i>DEPLOY_CARD</i></span></li>
	 * </ol>
	 */
	public static enum MoveType {
		SKIP, DRAW, ATTACK, ATTACK_CHARACTER, DEPLOY_CARD
	}

	/**
	 * Type of move the current move is.
	 */
	private MoveType type;

	/**
	 * IF APPLICABLE, the card to be deployed from the Opponent's deck.
	 */
	private Card deployCard;
	/**
	 * IF APPLICABLE, the slot where the card will be deployed. Zero-indexed (1st slot is 0, 2nd slot is 1, etc).
	 */
	private int deployTarget;

	/**
	 * IF APPLICABLE, the card to attack with in the Opponent's deck. Zero-indexed (1st slot is 0, 2nd slot is 1, etc).
	 */
	private int attacker;
	/**
	 * IF APPLICABLE, the slot of the card to attack. Zero-indexed (1st slot is 0, 2nd slot is 1, etc).
	 */
	private int attackTarget;

	/**
	 * IF APPLICABLE, the Character who will be attacking.
	 */
	private Character characterAttacker;
	/**
	 * IF APPLICABLE, the Character who will be attacked.
	 */
	private Character characterTarget;

	/**
	 * Constructor for the OpponentMove type.
	 * 
	 * @param t The type of Move to be performed. {@code MoveType} is a public enum
	 * located in the {@code OpponentMove} class.
	 */
	public OpponentMove(MoveType t) {
		this.type = t;
	}


	// GETTERS & SETTERS
	public void setType(MoveType type) {
		this.type = type;
	}

	public void setDeployCard(Card deployCard) {
		this.deployCard = deployCard;
	}

	public void setAttacker(int attacker) {
		this.attacker = attacker;
	}

	public void setDeployTarget(int deployTarget) {
		this.deployTarget = deployTarget;
	}

	public MoveType getType() {
		return type;
	}

	public Card getDeployCard() {
		return deployCard;
	}

	public int getAttacker() {
		return attacker;
	}

	public int getDeployTarget() {
		return deployTarget;
	}

	public int getAttackTarget() {
		return attackTarget;
	}

	public void setAttackTarget(int attackTarget) {
		this.attackTarget = attackTarget;
	}

	public Character getCharacterAttacker() {
		return characterAttacker;
	}

	public void setCharacterAttacker(Character characterAttacker) {
		this.characterAttacker = characterAttacker;
	}

	public Character getCharacterTarget() {
		return characterTarget;
	}

	public void setCharacterTarget(Character characterTarget) {
		this.characterTarget = characterTarget;
	}
}
