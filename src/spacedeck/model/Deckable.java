/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package spacedeck.model;

/**
 * Interface that refers to any item that can be placed inside a deck. Requires
 * a name, description, icon, and cost. Includes cards and gears
 * @author pj
 */
public interface Deckable {
    String getName();
    String getDescription();
    String getIcon();
    int getCost();
}
