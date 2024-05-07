/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package spacedeck.battlescreen;

import spacedeck.model.Card;
import spacedeck.model.Deckable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author pj
 */
public class CardPanelController implements Initializable {
    private TranslateTransition cardHoverEnter;
    private RotateTransition cardPickup, cardRelease;
    @FXML
    private VBox card;
    private Deckable thisCard;
    private double restingTranslate = 0d;
    private double restingRotate;
    private double startX, startY;
    private boolean isBeingDragged = false;
    
    private BattleScreenController sceneController;
	@FXML
	private Text costElement;
	@FXML
	private ImageView iconElement;
	@FXML
	private Text nameElement;
	@FXML
	private Text descriptionElement;
	@FXML
	private ImageView element;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        // Initializes the cardHoverEnter transition        
        cardHoverEnter = new TranslateTransition();
        cardHoverEnter.setAutoReverse(true);
        cardHoverEnter.setCycleCount(2);
        cardHoverEnter.setDuration(Duration.seconds(0.2));
        
        cardPickup = new RotateTransition();
        cardPickup.setDuration(Duration.seconds(0.03));
        cardPickup.setNode(card);
    }    

    @FXML
    private void cardHoverEnterAnimation(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!sceneController.getIsScreenActive()) return;
		if (!sceneController.isPlayerTurn()) return;

        // If the card is not currently being dragged
        // Here bc if it is being dragged, and the card lags behind and then catches up, the cardHoverEnterAnimation
        // starts playing again (we don't want that)
        if (!isBeingDragged) {
            // The translation depends on the orientation of the card
            // If the card is rotated to the left slightly, it will pertrude to the direction it's pointing at
            // -card.getRotate() is the rotation of the card ignoring the sign
            // Divide by 360 and multiply by 2pi to get it in radians
            double rotationFactor = -card.getRotate() / 360 * 2 * Math.PI;
        
            cardHoverEnter.setNode(card);
            
            // The card is translated by how much it is rotated in that direction, but in a 90 degree offset
            // This is because angles are measured from the +x direction, but the card at 0 degree rotation is upright (90 degrees)
            // Thus, setByY is cos and setByX is sin
            cardHoverEnter.setByY(-Math.cos(rotationFactor) * 5);
            cardHoverEnter.setByX(-Math.sin(rotationFactor) * 5);

            // Get each card that is NOT the current card,
            // Set the opacity to a low value
            for (Node currentCard : (ObservableList<Node>) card.getParent().getChildrenUnmodifiable()) {
                if (currentCard != card) {
                    currentCard.setOpacity(0.1);
                }
            }

            cardHoverEnter.play();
        }
    }
    
    @FXML
    private void cardHoverExitAnimation(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!sceneController.getIsScreenActive()) return;

        if (!isBeingDragged) {
            // Get each card that is NOT the current card,
            // Revert the opacity
            for (Node currentCard : (ObservableList<Node>) card.getParent().getChildrenUnmodifiable()) {
                if (currentCard != card) {
                    currentCard.setOpacity(1);
                }
            }
        }
    }
    
    @FXML
    private void cardPress(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!sceneController.getIsScreenActive()) return;

        if (event.getButton() == MouseButton.PRIMARY) {
            startX = event.getSceneX();
            startY = event.getSceneY();
            isBeingDragged = true;

            restingTranslate = card.getTranslateX();

            restingRotate = card.getRotate();
            cardPickup.setFromAngle(restingRotate);
            cardPickup.setToAngle(0);
            cardPickup.play();
            sceneController.setIsDraggingCard(true);
        }
    }
    
    @FXML
    private void cardDrag(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!sceneController.getIsScreenActive()) return;

        card.setTranslateX(restingTranslate + event.getSceneX() - startX);
        card.setTranslateY(event.getSceneY() - startY);
    }
    
    @FXML
    private void cardRelease(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!sceneController.getIsScreenActive()) return;

        if (event.getButton() == MouseButton.PRIMARY) {
            isBeingDragged = false;
            
            // Return the card to its initial position
            card.setTranslateX(restingTranslate);
            card.setTranslateY(0);
            card.setRotate(restingRotate);

            cardHoverExitAnimation(event);
            sceneController.setIsDraggingCard(false);
            
            if (thisCard instanceof Card) {
                sceneController.setToSlot(event, this);
            }
        }
    }

	public void updateAttributes(){
		costElement.setText(Integer.toString(thisCard.getCost()));
		Image icon = new Image(getClass().getResourceAsStream("/spacedeck/media/" + thisCard.getIcon()));
		iconElement.setImage(icon);
		nameElement.setText(thisCard.getName());
		descriptionElement.setText(thisCard.getDescription());
		String elementName = ((Card)thisCard).getElement();
		Image elementIcon = null;
		if ("Fire".equals(elementName)) {
			elementIcon = new Image(getClass().getResourceAsStream("/spacedeck/media/Fire Icon.png"));
		}
		if ("Water".equals(elementName)) {
			elementIcon = new Image(getClass().getResourceAsStream("/spacedeck/media/Water Icon.png"));
		}
		if ("Earth".equals(elementName)) {
			elementIcon = new Image(getClass().getResourceAsStream("/spacedeck/media/Earth Icon.png"));
		}
		if ("Air".equals(elementName)) {
			elementIcon = new Image(getClass().getResourceAsStream("/spacedeck/media/Air Icon.png"));
		}
		element.setImage(elementIcon);
	}
    
    public void setSceneController(BattleScreenController controller) {
        this.sceneController = controller;
    }
    
    public void setCard(Deckable c) {
        thisCard = c;
		updateAttributes();
    }
    
    public Deckable getCard() {
        return thisCard;
    }
    
    public VBox getSceneCard() {
        return card;
    }
    
    // Method that DESTROYS THE OBJECT
    public void deinitialize() {
        card.getChildren().clear();
        card.getStyleClass().clear();
        card.setMaxSize(0, 0);
    }
    
}
