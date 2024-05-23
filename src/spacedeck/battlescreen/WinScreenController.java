/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package spacedeck.battlescreen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author pj
 */
public class WinScreenController implements Initializable {
	@FXML
	private GridPane rewardsList;

	private BattleScreenController sceneController;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) { }	

	public void setSceneController(BattleScreenController b) {
		sceneController = b;
	}

	public GridPane getRewardsList() {
		return rewardsList;
	}

	@FXML
	private void onHoverExit(MouseEvent event) {
		sceneController.onMouseHoverExit(event);
	}

	@FXML
	private void onHoverEnter(MouseEvent event) {
		sceneController.onMouseHoverEnter(event);
	}

	@FXML
	private void backToMenu(MouseEvent event) {
		sceneController.backToMenu(event);
	}
	
}
