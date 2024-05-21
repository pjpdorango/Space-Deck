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
import spacedeck.battlescreen.BattleScreenController.GameState;

/**
 * FXML Controller class
 *
 * @author pj
 */
public class StackCardPanelController implements Initializable {
	private BattleScreenController sceneController;
	
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
			
	}	

	@FXML
	private void getCardFromStack(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (sceneController.hasPlayerDrawnCard()) return;
		if (sceneController.getState() != GameState.GAME) return;

		sceneController.setPlayerDrawnCard(true);

		sceneController.getCardFromStack(event, sceneController.getPlayer()).forEach((e) -> {
			sceneController.playEvents(e);
		});
	}


	public void setSceneController(BattleScreenController cont) {
		this.sceneController = cont;
	}

}
