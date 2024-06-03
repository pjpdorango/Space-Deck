/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

import java.util.ArrayList;

/**
 * Helper class to keep track of every Level's owner planet, opponent, and rewards.
 * 
 * @author pj
 */
public class Level {
	/**
	 * The opponent for this level.
	 */
	private Opponent opponent;
	/**
	 * The planet which owns this level.
	 */
	private Planet planet;
	/**
	 * The rewards received when the level is beaten.
	 */
	private ArrayList<Item> rewards = new ArrayList<>();
	
	/**
	 * Constructor for the Level class.
	 * 
	 * @param e The opponent of the level.
	 */
	public Level(Opponent e) {
		opponent = e;
	}	

	public Opponent getOpponent() {
		return opponent;
	}

	public void setOpponent(Opponent opponent) {
		this.opponent = opponent;
	}

	public ArrayList<Item> getRewards() {
		return rewards;
	}

	public void setPlanet(Planet p) {
		planet = p;
	}

	public Planet getPlanet() {
		return this.planet;
	}
}
