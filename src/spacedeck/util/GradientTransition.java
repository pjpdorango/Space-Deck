/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.util;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
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
		ArrayList<Color> colors = new ArrayList<>();

		originalGradient.getStops().forEach((s) -> {
			colors.add(s.getColor());
		});

		updatedGradient.getStops().forEach((s) -> {
			int index = updatedGradient.getStops().indexOf(s);
			
			colors.set(index, colors.get(index).interpolate(updatedGradient.getStops().get(index).getColor(), frac));
		});

		if (node instanceof Rectangle) {
			interpolateColor(frac, colors);
		} else {
			interpolateStyle(frac, colors);
		}
	}

	public void interpolateColor(double frac, ArrayList<Color> colors) {
		Rectangle rect = (Rectangle) node;
		List<Stop> stopList = new ArrayList<>();
		for (int i = 0; i < colors.size(); i++) {
			stopList.add(new Stop(originalGradient.getStops().get(i).getOffset(), colors.get(i)));
		}

		rect.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stopList));
	}

	public void interpolateStyle(double frac, ArrayList<Color> colors) {
		String origStyle = node.getStyle();

		String newStyle = "-fx-background-color: linear-gradient(to bottom right, ";
		for (int i = 0; i < colors.size(); i++) {
			newStyle += "rgb(";
			newStyle += colors.get(i).getRed() * 255 + ", ";
			newStyle += colors.get(i).getGreen() * 255 + ", ";
			newStyle += colors.get(i).getBlue() * 255;
			newStyle += ")";

			if (i != colors.size() - 1) newStyle += ", ";
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
