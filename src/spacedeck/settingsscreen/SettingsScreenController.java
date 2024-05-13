/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package spacedeck.settingsscreen;

import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import spacedeck.SpaceDeck;

/**
 * FXML Controller class
 *
 * @author mika
 */
public class SettingsScreenController implements Initializable {

	@FXML
	private ScrollBar masterVolumeScrollbar;
	@FXML
	private ScrollBar musicScrollbar;
	@FXML
	private ScrollBar sfxScrollbar;
	@FXML
	private Text resolution;

	private final int[] DEFAULT_RESOLUTION = { 960 , 540 };
	private boolean isScreenActive = true;

	private ArrayList<Double> scales = new ArrayList<>();
	private int currentResolutionIndex;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		masterVolumeScrollbar.setValue((double) SpaceDeck.getSetting("masterVolume"));
		musicScrollbar.setValue((double) SpaceDeck.getSetting("musicVolume"));
		sfxScrollbar.setValue((double) SpaceDeck.getSetting("sfxVolume"));

		JSONObject settings = (JSONObject) SpaceDeck.fastOpenJSON("src/spacedeck/Settings.json");
		scales.add(2d/3d);
		scales.add(0.75);
		scales.add(1d);
		scales.add(1.25);
		scales.add(1.50);

		double scale = (double) settings.get("scale");
		currentResolutionIndex = scales.indexOf(scale);
		double width = SpaceDeck.DEFAULT_RESOLUTION[0] * scale;
		double height = SpaceDeck.DEFAULT_RESOLUTION[1] * scale;
		resolution.setText((int) width + "x" + (int) height);
    }    

	@FXML
	private void leftResolutionCycle(MouseEvent event) {
		if (currentResolutionIndex == 0) return;

		currentResolutionIndex--;
		updateResolution();
	}

	@FXML
	private void rightResolutionCycle(MouseEvent event) {
		if (currentResolutionIndex == scales.size() - 1) return;

		currentResolutionIndex++;
		updateResolution();
	}
    
	private void updateResolution() {
		double width, height;
		double scale = scales.get(currentResolutionIndex);
		width = SpaceDeck.DEFAULT_RESOLUTION[0] * scale;
		height = SpaceDeck.DEFAULT_RESOLUTION[1] * scale;

		resolution.setText((int) width + "x" + (int) height);

		Parent root = SpaceDeck.getStage().getScene().getRoot();
		root.setScaleX(scale);
		root.setScaleY(scale);
		root.setLayoutX(SpaceDeck.DEFAULT_RESOLUTION[0] * (scale - 1) / 2 + 120 * Math.abs(scale - 1)); //magic numbers, sorry thru trial & error not math
		root.setLayoutY(SpaceDeck.DEFAULT_RESOLUTION[1] * (scale - 1) / 2 + 40 * Math.abs(scale - 1));	// the formula (origX * (scale - 1))/2 was from math. the 120 and 60 were magic numbers

		SpaceDeck.getStage().setX(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - width / 2);
		SpaceDeck.getStage().setY(Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - height / 2);
		SpaceDeck.getStage().setWidth(width + 10);
		SpaceDeck.getStage().setHeight(height + 30);

		SpaceDeck.changeSetting("scale", scale);
	}

	@FXML
	private void updateScrollbar(MouseEvent event) {
		ScrollBar scrollbar = (ScrollBar) event.getSource();
		String key = null;

		if (scrollbar == masterVolumeScrollbar) key = "masterVolume";
		else if (scrollbar == musicScrollbar) key = "musicVolume";
		else if (scrollbar == sfxScrollbar) key = "sfxVolume";

		SpaceDeck.changeSetting(key, scrollbar.getValue());
	}

	@FXML
	private void backToTitleScreen(MouseEvent event) {
		if (!isScreenActive) return;

		isScreenActive = false;
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.TitleScreen);
	}
}
