/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.battlescreen;

import spacedeck.model.Deckable;
import javafx.animation.Transition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.Node;

/**
 *
 * @author pj
 */
public class CardStackTransition extends Transition {
	private VBox card, newCard;
	private double byX, byY;
	private double origX, origY;
	private boolean hasTransformed, hasInitialized;
	private BattleScreenController sceneController;
	private Deckable deckableCard;

	@Override
	public void interpolate(double frac) {
		if (frac > 0.5 && !hasTransformed) {
			newCard.setTranslateX(card.getTranslateX());
			newCard.setTranslateY(card.getTranslateY());
			((AnchorPane) card.getParent()).getChildren().add(newCard);
			((AnchorPane) card.getParent()).getChildren().remove(card);
			card = newCard;
			hasTransformed = true;
		}
		
		card.setTranslateX(origX + byX * frac);
		card.setTranslateY(origY + byY * frac);		

		if (hasTransformed) {
			card.setScaleX(frac * 2 - 1);
		} else {
			card.setScaleX(1 - frac * 2);
		}
	}
	
	public void setNode(Node c) {
		this.card = (VBox) c;	
		this.hasTransformed = false;
		this.hasInitialized = false;
	}

	public void setNewNode(Node n) {
		newCard = (VBox) n;
	}

	public void setByX(double x) {
		byX = x;
		origX = -byX;
	}

	public void setByY(double y) {
		byY = y;
		origY = -byY;
	}

	public void setDuration(Duration duration) {
		setCycleDuration(duration);
	}

	public void setSceneController(BattleScreenController c) {
		sceneController = c;
	}

	public void setDeckable(Deckable d) {
		deckableCard = d;
	}
}
