/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.battlescreen;

import javafx.animation.Transition;
import javafx.geometry.Bounds;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.Node;

/**
 *
 * @author pj
 */
public class OpponentDrawTransition extends Transition {
	private VBox card;
	private double byX, byY;
	private double origX, origY;
	private boolean hasInitialized;

	@Override
	public void interpolate(double frac) {
		card.setTranslateX(origX + byX * (1 - frac));
		card.setTranslateY(origY + byY * (1 - frac));		
	}
	
	public void setNode(Node c) {
		this.card = (VBox) c;	
		this.hasInitialized = false;
	}

	public void setOrig(Bounds b) {
		origX = b.getMinX();
		origY = b.getMinY();
	}

	public void setByX(double x) {
		byX = -x;
	}

	public void setByY(double y) {
		byY = -y;
	}

	public void setDuration(Duration duration) {
		setCycleDuration(duration);
	}
}
