package spacedeck.titlescreen;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.util.Duration;
import spacedeck.SpaceDeck;

/**
 * FXML Controller class
 *
 * @author pj
 */
public class TitleScreenController implements Initializable {
    
    private ScaleTransition mouseExit, mouseEnter;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        mouseEnter = new ScaleTransition();
        mouseExit = new ScaleTransition();
        mouseEnter.setDuration(new Duration(20));
        mouseExit.setDuration(new Duration(20));
        
        mouseEnter.setFromX(1);
        mouseEnter.setFromY(1);
        
        mouseExit.setFromX(1.2);
        mouseExit.setFromY(1.2);
        
        mouseEnter.setToX(1.2);
        mouseEnter.setToY(1.2);
        
        mouseExit.setToX(1);
        mouseExit.setToY(1);
    }    

    @FXML
    private void textMouseExit(MouseEvent event) {
        mouseExit.setNode((Node) event.getSource());
        mouseExit.play();
    }

    @FXML
    private void textMouseEnter(MouseEvent event) {
        mouseEnter.setNode((Node) event.getSource());
        mouseEnter.play();
    }

	@FXML
	private void playButton(MouseEvent event) {
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.MapScreen);
	}

	@FXML
	private void collectionButton(MouseEvent event) {
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.CollectionScreen);
	}

	@FXML
	private void settingsButton(MouseEvent event) {
	}

	@FXML
	private void exitButton(MouseEvent event) {
		System.exit(0);
	}
    
}
