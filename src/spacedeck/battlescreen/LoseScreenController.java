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

/**
 * FXML Controller class
 *
 * @author pj
 */
public class LoseScreenController implements Initializable {
	private BattleScreenController sceneController;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) { }	

	@FXML
	private void onMouseHoverExit(MouseEvent event) {
		sceneController.onMouseHoverExit(event);
	}

	@FXML
	private void onMouseHoverEnter(MouseEvent event) {
		sceneController.onMouseHoverEnter(event);
	}

	@FXML
	private void restartGame(MouseEvent event) {
		sceneController.restartGame(event);
	}

	@FXML
	private void backToMenu(MouseEvent event) {
		sceneController.backToMenu(event);
	}

	public void setSceneController(BattleScreenController b) {
		sceneController = b;
	}
}
