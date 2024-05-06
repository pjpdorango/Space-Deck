/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

/**
 *
 * @author pj
 */
public class OpponentMove {
	public static enum MoveType {
		SKIP, DRAW, ATTACK, EQUIP_GEAR, DEPLOY_CARD, REMOVE_CARD
	}

	private MoveType type;
	private Card deployCard;
	private int attacker;
	private int deployTarget, removeTarget;
	private Gear gear;
	private Card equipGearTarget;

	/**
	 * Constructor for the OpponentMove type.
	 * <b> NOTE: </b>
	 * Not all parameters are set from the get-go. Some other attributes must be
	 * set depending on the move type
	 * 
	 * <ol>
	 * 	<li> {@code SKIP} - No attributes needed. </li>
	 * 	<li> {@code DRAW} - No attributes needed. </li>
	 * 	<li> {@code ATTACK} - Requires {@code attacker} to be set. </li>
	 * 	<li> {@code EQUIP_GEAR} - Requires {@code gear} and {@code equipGearTarget} to be set. </li>
	 * 	<li> {@code DEPLOY_CARD} - Requires {@code deployCard} and {@code deployTarget} to be set. </li>
	 * 	<li> {@code REMOVE_CARD} - Requires {@code removeTarget} to be set. </li>
	 * </ol>
	 * 
	 * @param t The type of Move to be performed. {@code MoveType} is a public enum
	 * located in the {@code OpponentMove} class.
	 */
	public OpponentMove(MoveType t) {
		this.type = t;
		this.deployCard = null;
		this.deployTarget = 0;
		this.removeTarget = 0;
		this.equipGearTarget = null;
	}

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

	public void setRemoveTarget(int removeTarget) {
		this.removeTarget = removeTarget;
	}

	public void setGear(Gear gear) {
		this.gear = gear;
	}

	public void setEquipGearTarget(Card equipGearTarget) {
		this.equipGearTarget = equipGearTarget;
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

	/**
	 * @return {@code deployTarget} - {@code int} that represents the slot where the
	 * card shall be deployed. 0-indexed (1st slot is 0, 2nd slot is 1, etc).
	 */
	public int getDeployTarget() {
		return deployTarget;
	}

	public int getRemoveTarget() {
		return removeTarget;
	}

	public Gear getGear() {
		return gear;
	}

	public Card getEquipGearTarget() {
		return equipGearTarget;
	}
}
