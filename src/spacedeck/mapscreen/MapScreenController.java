/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package spacedeck.mapscreen;

import spacedeck.levelselectionscreen.LevelSelectionScreenController;
import spacedeck.model.Planet;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.json.simple.JSONObject;
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

	private boolean isScreenActive;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		ImageView[] planets = new ImageView[] { cryoterra, lunarisIX, aurosMinoris, astraforge };

		isScreenActive = true;

		JSONObject profile = (JSONObject) SpaceDeck.fastOpenJSON("src/spacedeck/Profile.json");

		JSONObject completed = (JSONObject) profile.get("completed");
		int unlocked = (int) (long) completed.get("planet");

		for (int i = 0; i < planets.length; i++) {
			if (i >= unlocked) {
				disablePlanet(planets[i]);
			}
		}
	}

	private void disablePlanet(ImageView planet) {
		makeGreyScale(planet);
		planet.setOnMouseEntered(null);
		planet.setOnMouseExited(null);
		planet.setOnMouseClicked(null);
	}

	@FXML
	private void goToPlanet(MouseEvent event) {
		// Only do if screen is active
		if (!isScreenActive) return;

		isScreenActive = false;
		JSONObject profile = (JSONObject) SpaceDeck.fastOpenJSON("src/spacedeck/Profile.json");
		int unlocked = (int) (long) ((JSONObject) profile.get("completed")).get("planet");

		Planet destination;
		
		ImageView planet = (ImageView) event.getSource();
		if (planet == cryoterra && unlocked <= 1) {
			destination = Planet.searchPlanet("Cryoterra");
		} else if (planet == lunarisIX && unlocked <= 2) {
			destination = Planet.searchPlanet("Lunaris IX");
		} else if (planet == aurosMinoris && unlocked <= 3) {
			destination = Planet.searchPlanet("Auros Minoris");
		} else if (planet == astraforge && unlocked <= 4) {
			destination = Planet.searchPlanet("Astraforge");
		} else return;

		FXMLLoader levelSelectLoader = SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.LevelSelectionScreen);
		LevelSelectionScreenController levelSelectController = levelSelectLoader.getController();
		levelSelectController.setPlanet(destination);
	}

	@FXML
	private void mouseHoverEnter(MouseEvent event) {
		// Only do if screen is active
		if (!isScreenActive) return;

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
		// Only do if screen is active
		if (!isScreenActive) return;

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

	@FXML
	private void returnToMenu(MouseEvent event) {
		// Only do if screen is active
		if (!isScreenActive) return;

		isScreenActive = false;
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.TitleScreen);
	}

	private void makeGreyScale(ImageView img) {
		Image image = img.getImage();
		
		PixelReader px = image.getPixelReader();
		WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
		PixelWriter pwNewImage = newImage.getPixelWriter();

		System.out.println("GREYING START!");
		for (int i = 0; i < (int) image.getWidth(); i++) {
			for (int j = 0; j < (int) image.getHeight(); j++) {
				Color color = px.getColor(i, j);
				// Magic numbers make the values grey scale relative to human sight
				// I looked this up on google dont judge
				// Also i was too lazy to make the grey scale images properly and this was more fun
				double greyColor = color.getRed() * 0.3 + color.getBlue() * 0.59 + color.getGreen() * 0.11;
				pwNewImage.setColor(i, j, new Color(greyColor, greyColor, greyColor, color.getOpacity()));
			}
		}
		System.out.println("GREYING SUCCESS!");
		
		img.setImage(newImage);
	}
    
}
