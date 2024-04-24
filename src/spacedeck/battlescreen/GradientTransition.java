/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.battlescreen;

import java.util.ArrayList;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.paint.LinearGradient;
import javafx.util.Duration;

/**
 *
 * @author pj
 */
public class GradientTransition extends Transition {
	private LinearGradient originalGradient;
	private LinearGradient updatedGradient;
	private Node node;

	public void setDuration(Duration d) {
		this.setCycleDuration(d.multiply(2));
	}
	
	public void interpolate(double frac) {
		String origStyle = node.getStyle();
		ArrayList<Double> r = new ArrayList<>(), g = new ArrayList<>(), b = new ArrayList<>();

		originalGradient.getStops().forEach((s) -> {
			r.add(s.getColor().getRed() * 255);
			g.add(s.getColor().getGreen() * 255);
			b.add(s.getColor().getBlue() * 255);
		});

		updatedGradient.getStops().forEach((s) -> {
			int index = updatedGradient.getStops().indexOf(s);
			
			r.set(index, r.get(index) - (r.get(index) - s.getColor().getRed() * 255) * frac);
			g.set(index, g.get(index) - (g.get(index) - s.getColor().getGreen() * 255) * frac);
			b.set(index, b.get(index) - (b.get(index) - s.getColor().getBlue() * 255) * frac);
		});


		String newStyle = "-fx-background-color: linear-gradient(to bottom right, ";
		for (int i = 0; i < r.size(); i++) {
			newStyle += "rgb(";
			newStyle += r.get(i) + ", ";
			newStyle += g.get(i) + ", ";
			newStyle += b.get(i);
			newStyle += ")";

			if (i != r.size() - 1) newStyle += ", ";
		}
		newStyle += ");";

		node.setStyle(newStyle);
	}

	public LinearGradient getOriginalGradient() {
		return originalGradient;
	}

	public void setOriginalGradient(LinearGradient originalGradient) {
		this.originalGradient = originalGradient;
	}

	public LinearGradient getUpdatedGradient() {
		return updatedGradient;
	}

	public void setUpdatedGradient(LinearGradient updatedGradient) {
		this.updatedGradient = updatedGradient;
	}

	public void setNode(Node node) {
		this.node = node;
	}
}
