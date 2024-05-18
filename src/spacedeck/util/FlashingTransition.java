/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.util;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Red flashing transition for animations depicting an invalid action.
 * <h3> How to Use </h3>
 * <p>
 * The constructor takes an input {@code c} that is the style class to apply
 * to the object as the "red" animation.
 * </p>
 * 
 * <p>
 * For example, to make a flashing red text, the style class would be {@code "-fx-text-color"}.
 * Or, to make a flashing red border, the style class would be {@code "-fx-border-color"}.
 * </p>
 * 
 * <p>
 * Before using {@code .play()} on the class, make sure to first set a {@code node} which
 * will be the node to apply to style class on.
 * </p>
 * @author pj
 */
public class FlashingTransition extends Transition {
    private Node node;
    String classToApply;
    
	/**
	 * @param c 
	 * The class that houses the "red" effect that will be flashed onto the node.
	 * 
	 * <p>
	 * For example, to make a flashing red text, the style class would be {@code "-fx-text-color"}.
	 * </p>
	 * 
	 * <p>
	 * Or, to make a flashing red border, the style class would be {@code "-fx-border-color"}.
	 * </p>
	 */
    public FlashingTransition(String c) {
        this.node = null;
        this.classToApply = c;
		this.setCycleDuration(Duration.millis(200));
    }
    
    public void setDuration(Duration value) {
        this.setCycleDuration(value.multiply(2));
    }
    
    @Override
    protected void interpolate(double fraction) {
        if (fraction <= 0.5) {
            if (!node.getStyleClass().contains(classToApply)) {
                node.getStyleClass().add(classToApply);
            }
        } else {
            node.getStyleClass().remove(classToApply);
        }
    }
    
    public void setNode(Node n) {
        this.node = n;
    }
}
