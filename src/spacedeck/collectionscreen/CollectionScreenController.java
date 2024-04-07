/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package spacedeck.collectionscreen;

import spacedeck.model.WaterCard;
import spacedeck.model.EarthCard;
import spacedeck.model.Card;
import spacedeck.model.Player;
import spacedeck.model.FireCard;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import spacedeck.SpaceDeck;

/**
 *
 * @author mika
 */
public class CollectionScreenController implements Initializable {
    
    @FXML
    private GridPane collectionGrid;
    private Player currentPlayer;
    private final int GRID_ROWS = 3, GRID_COLS = 4;
    @FXML
    private ImageView selectedCardIcon;
    @FXML
    private Text selectedCardName;
    @FXML
    private Text selectedCardCost;
    @FXML
    private Text selectedCardHealth;
    @FXML
    private Text selectedCardAttack;
    @FXML
    private Text selectedCardDescription;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentPlayer = new Player("Awesome", 20, 3);
        currentPlayer.addToCollection(Card.getRandomCard());
        currentPlayer.addToCollection(Card.getRandomCard());
        currentPlayer.addToCollection(Card.getRandomCard());
        currentPlayer.addToCollection(Card.getRandomCard());
        currentPlayer.addToCollection(Card.getRandomCard());
        currentPlayer.addToCollection(Card.getRandomCard());
        currentPlayer.addToCollection(Card.getRandomCard());
        currentPlayer.addToCollection(Card.getRandomCard());
        currentPlayer.addToCollection(Card.getRandomCard());
        
        addCardsToGrid();
        setSelectedCard(currentPlayer.getCollection().get(0));
    }    
    
    private void addCardsToGrid() {
        try {
            for (int i = 0; i < currentPlayer.getCollection().size(); i++) {
                Card card = currentPlayer.getCollection().get(i);
                
                FXMLLoader cardPanelLoader = new FXMLLoader(getClass().getResource("CardDisplayPanel.fxml"));
                VBox cardPanel = cardPanelLoader.load();
                CardDisplayPanelController controller = cardPanelLoader.getController();
                
                controller.setSceneController(this);
                controller.setCard(card);
                collectionGrid.add(cardPanel, i % (GRID_COLS), i / (GRID_COLS));
                
                cardPanel.setScaleX(0.75);
                cardPanel.setScaleY(0.75);
            }
        } catch (IOException e) {
            System.out.println("[ERROR] IOException at addCardsToGrid()");
            
        }
    }
    
    public void setSelectedCard(Card c) {
        selectedCardName.setText(c.getName());
        selectedCardCost.setText(Integer.toString(c.getCost()));
        selectedCardHealth.setText(Integer.toString(c.getHealth()));
        selectedCardAttack.setText(Integer.toString(c.getAttack()));
        selectedCardDescription.setText(c.getDescription());
        Image selectedCardImage = new Image(getClass().getResourceAsStream("/spacedeck/media/" + c.getIcon()));
        selectedCardIcon.setImage(selectedCardImage);
    }

	@FXML
	private void returnToMenu(MouseEvent event) {
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.TitleScreen);
	}
    
}
