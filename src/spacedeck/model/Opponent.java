/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

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
    
    @Override
    public void attack(Character ch) {
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
        // take each playing card and attack the opponents side
    }
    
}
