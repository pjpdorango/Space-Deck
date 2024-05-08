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

	private ArrayList<Double> scales = new ArrayList<>();
	private int currentResolutionIndex;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		masterVolumeScrollbar.setValue(70);
		musicScrollbar.setValue(100);
		sfxScrollbar.setValue(100);

		JSONObject settings = (JSONObject) SpaceDeck.fastOpenJSON("src/spacedeck/Settings.json");
		scales.add(0.5);
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
		currentResolutionIndex--;
		if (currentResolutionIndex < 0) currentResolutionIndex = scales.size() - 1;

		updateResolution();
	}

	@FXML
	private void rightResolutionCycle(MouseEvent event) {
		currentResolutionIndex++;
		if (currentResolutionIndex >= scales.size()) currentResolutionIndex = 0;

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
		root.setLayoutX(SpaceDeck.DEFAULT_RESOLUTION[0] * (scale - 1) / 2 + 120 * Math.abs(scale - 1)); //magic numbers, sorry
		root.setLayoutY(SpaceDeck.DEFAULT_RESOLUTION[1] * (scale - 1) / 2 + 60 * Math.abs(scale - 1)); //thru trial & error not math

		SpaceDeck.getStage().setX(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - width / 2);
		SpaceDeck.getStage().setY(Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - height / 2);
		SpaceDeck.getStage().setWidth(width);
		SpaceDeck.getStage().setHeight(height);

		System.out.println("Width: " + SpaceDeck.getStage().getWidth());
		System.out.println("RootWidth: " + root.localToScene(root.getBoundsInLocal()).getWidth());

		SpaceDeck.changeSetting("scale", scale);
	}
}
