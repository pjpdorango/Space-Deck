/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

import java.util.ArrayList;

/**
 *
 * @author pj
 */
public class Level {
	private Opponent opponent;
	private ArrayList<Item> rewards = new ArrayList<>();
	
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
}
