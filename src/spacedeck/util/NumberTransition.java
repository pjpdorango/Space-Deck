/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.util;

import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.Transition;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author pj
 */
public class NumberTransition extends Transition {
	private int start, end;
	private Text text;
	private AtomicReference<Integer> number;

	@Override
	protected void interpolate(double frac) { 
		int middle = (int) (start + (end - start) * frac);
		if (text != null) text.setText(Integer.toString(middle));
		if (number != null) number.set(middle);
	}

	public void setStart(int s) {
		start = s;
	}

	public void setEnd(int e) {
		end = e;
	}

	public void setNode(Text t) {
		text = t;
	}

	public void setDuration(Duration d) {
		setCycleDuration(d.divide(getCycleCount()));
	}

	public void setNumberReference(AtomicReference<Integer> n) {
		number = n;
	}
}
