/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package spacedeck.collectionscreen;

import spacedeck.model.Card;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author pj
 */
public class CardDisplayPanelController implements Initializable {
    @FXML
    private VBox card;
    @FXML
    private Text cardCost;
    @FXML
    private ImageView cardIcon;
    @FXML
    private Text cardName;
    @FXML
    private Text cardDescription;
    
    private Card thisCard;
    private CollectionScreenController sceneController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { }
    
    public void setSceneController(CollectionScreenController cont) {
        sceneController = cont;
    }

    @FXML
    private void selectCard(MouseEvent event) {
        sceneController.setSelectedCard(thisCard);
    }
    
    public void setCard(Card c) {
        thisCard = c;
		cardCost.setText(Integer.toString(c.getCost()));
		cardIcon.setImage(new Image(getClass().getResourceAsStream("/spacedeck/media/" + c.getIcon())));
		cardName.setText(c.getName());
		cardDescription.setText(c.getDescription());
    }
}

