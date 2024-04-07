/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package spacedeck.mapscreen;

import spacedeck.levelselectionscreen.LevelSelectionScreenController;
import spacedeck.model.Planet;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import spacedeck.SpaceDeck;

/**
 *
 * @author mika
 */
public class MapScreenController implements Initializable {
    
	@FXML
	private ImageView cryoterra;
	@FXML
	private ImageView lunarisIX;
	@FXML
	private ImageView aurosMinoris;
	@FXML
	private ImageView astraforge;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { }

	@FXML
	private void goToPlanet(MouseEvent event) {
		FXMLLoader levelSelectLoader = SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.LevelSelectionScreen);
		AnchorPane levelSelectScreen = levelSelectLoader.getRoot();
		LevelSelectionScreenController levelSelectController = levelSelectLoader.getController();
		
		ImageView planet = (ImageView) event.getSource();
		if (planet == cryoterra) {
			levelSelectController.setPlanet(Planet.searchPlanet("Cryoterra"));
		} else if (planet == lunarisIX) {
			levelSelectController.setPlanet(Planet.searchPlanet("Lunaris IX"));
		} else if (planet == aurosMinoris) {
			levelSelectController.setPlanet(Planet.searchPlanet("Auros Minoris"));
		} else if (planet == astraforge) {
			levelSelectController.setPlanet(Planet.searchPlanet("Astraforge"));
		}
	}

	@FXML
	private void mouseHoverEnter(MouseEvent event) {
		RotateTransition planetRotation = new RotateTransition();
		Node planet = (Node) event.getSource();

		planetRotation.setFromAngle(0);
		planetRotation.setToAngle(10);
		planetRotation.setDuration(Duration.millis(100));
		planetRotation.setNode(planet);
		planetRotation.play();

		ScaleTransition planetScale = new ScaleTransition();
		planetScale.setFromX(1);
		planetScale.setFromY(1);
		planetScale.setToX(1.1);
		planetScale.setToY(1.1);
		planetScale.setDuration(Duration.millis(100));
		planetScale.setNode(planet);
		planetScale.play();
	}

	@FXML
	private void mouseHoverExit(MouseEvent event) {
		RotateTransition planetRotation = new RotateTransition();
		Node planet = (Node) event.getSource();

		planetRotation.setFromAngle(10);
		planetRotation.setToAngle(0);
		planetRotation.setDuration(Duration.millis(100));
		planetRotation.setNode(planet);
		
		ScaleTransition planetScale = new ScaleTransition();
		planetScale.setFromX(1.1);
		planetScale.setFromY(1.1);
		planetScale.setToX(1);
		planetScale.setToY(1);
		planetScale.setDuration(Duration.millis(100));
		planetScale.setNode(planet);
		planetScale.play();	planetRotation.play();
	}
    
}
