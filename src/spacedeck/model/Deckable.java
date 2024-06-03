/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package spacedeck.model;

/**
 * Interface that refers to any item that can be placed inside a deck. Requires
 * a {@code name}, {@code description}, {@code icon}, and {@code cost}. Includes Cards and Gears.
 * @author pj
 */
public interface Deckable {
    String getName();
    String getDescription();
    String getIcon();
    int getCost();
}
