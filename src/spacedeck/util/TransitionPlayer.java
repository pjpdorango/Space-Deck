/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.util;

import javafx.scene.Node;

/**
 *
 * @author pj
 */
public interface TransitionPlayer {
	void activate(Node n);
	void deactivate(Node n);
	void interpolate(Node n);
}
