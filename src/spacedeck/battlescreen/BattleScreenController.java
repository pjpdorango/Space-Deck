/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package spacedeck.battlescreen;

import spacedeck.model.Card;
import spacedeck.model.Opponent;
import spacedeck.model.Player;
import spacedeck.model.Gear;
import spacedeck.model.Deckable;
import spacedeck.model.AILevel;
import spacedeck.model.Character;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation.Status;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import spacedeck.SpaceDeck;
import spacedeck.exceptions.CardAlreadyActiveException;
import spacedeck.exceptions.FullDeckException;
import spacedeck.exceptions.InsufficientFuelException;

/**
 *
 * This controller will control the model for the game, as well as
 * being a general game manager for the battle screen specifically.
 * 
 * @author pj
 */
public class BattleScreenController implements Initializable {
	@FXML
	private AnchorPane root;
    @FXML
    private AnchorPane playerDeckElement;
    @FXML
    private AnchorPane opponentDeckElement;
    @FXML
    private HBox playerPlayingField;
    @FXML
    private Text opponentFuelCount;
    @FXML
    private Text turnIndicator;
    @FXML
    private Text playerFuelCount;
    @FXML
    private AnchorPane cardStackElement;
    @FXML
    private MediaView videoBackground;
	@FXML
	private VBox menu;
	
    private final int CARDS_IN_CARD_STACK = 10;
    private final double ROTATION_PER_CARD = -10;
    
    private Player player;
    private Character opponent;
    private boolean isDraggingCard;
	private boolean isScreenActive;
	private double musicVolume = 0.3;
    
    // TRANSITIONS
    private TranslateTransition slotInvalid;
    private ScaleTransition takeSlot;
    private RedFlashingTransition borderRedFlashing;
    private MediaPlayer songPlayer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		isScreenActive = true;

		// Video Setting
        Media backgroundVideo = new Media(getClass().getResource("/spacedeck/media/[TEMP] video bg.mp4").toExternalForm());
        MediaPlayer backgroundPlayer = new MediaPlayer(backgroundVideo);
        backgroundPlayer.setAutoPlay(true);
        backgroundPlayer.setCycleCount(Integer.MAX_VALUE);
        backgroundPlayer.play();
        videoBackground.setMediaPlayer(backgroundPlayer);

        // Field Initialization
        isDraggingCard = false;
        slotInvalid = new TranslateTransition();
        slotInvalid.setByX(-10);
        slotInvalid.setDuration(new Duration(100));
        slotInvalid.setCycleCount(6);
        slotInvalid.setAutoReverse(true);
        
        borderRedFlashing = new RedFlashingTransition("invalidSlot");
        borderRedFlashing.setCycleCount(3);
        borderRedFlashing.setDuration(new Duration(100));
        
        takeSlot = new ScaleTransition();
        takeSlot.setByX(0.1);
        takeSlot.setByY(0.1);
        takeSlot.setAutoReverse(true);
        takeSlot.setCycleCount(2);
        takeSlot.setDuration(new Duration(70));
        
        try {
            // Instantiate Player
            player = new Player("PJ", 20, 1);
            for (int i = 0; i < 1; i++) {
				player.addCard(Card.getRandomCard());
            }
            
            // Instantiate Opponent
            opponent = new Opponent("Bob Ross", 20, 3, AILevel.ADVANCED);
            for (int i = 0; i < 4; i++) {
           		opponent.addCard(Card.getRandomCard());
            }
        } catch (FullDeckException e) {
            System.out.println("[ERROR] FullDeckException");
        }
        
		// Create all cardPanel decks and stacks
		createPlayerDeck();
		createOpponentDeck();
		createCardStack();

        // Set the turn indicator
        turnIndicator.setText("Player's Turn");

        opponentFuelCount.setText(Integer.toString(opponent.getFuel()));
        playerFuelCount.setText(Integer.toString(player.getFuel()));
        
        // Play music
        Media battleMusic = new Media(getClass().getResource("/spacedeck/audio/TEMP_battle_music.mp3").toExternalForm());
        songPlayer = new MediaPlayer(battleMusic);
        songPlayer.setVolume(musicVolume);
        songPlayer.setAutoPlay(true);
        songPlayer.play();
    }    
    
    public void createCardStack() {
        // Just load a certain amount of cards onto the cardPanel stack
        cardStackElement.getChildren().clear();     
		try {
			for (int i = 0; i < CARDS_IN_CARD_STACK; i++) {
				FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("StackCardPanel.fxml"));
				VBox card = (VBox) cardLoader.load();
				((StackCardPanelController) cardLoader.getController()).setSceneController(this);
				cardStackElement.getChildren().add(card);

				final int CARD_STACK_WIDTH = 100;
				card.setTranslateY(CARD_STACK_WIDTH / CARDS_IN_CARD_STACK * i);
			}
		} catch (IOException e) {
            System.out.println("[ERROR] Error loading card stack");
        }
}
    
    public void createPlayerDeck() {
        // Clear the player deck
        playerDeckElement.getChildren().clear();
        ArrayList<Deckable> playerDeck = player.getDeck();
        try {
			for (int i = 0; i < playerDeck.size(); i++) {
				Deckable currentCard = playerDeck.get(i);
				
				FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("CardPanel.fxml"));
				VBox cardElement = cardLoader.load();
				CardPanelController cardController = cardLoader.getController();
				cardController.setSceneController(this);
				cardController.setCard(currentCard);
				cardController.updateAttributes();
				
				if (currentCard instanceof Gear) {
					cardElement.getStyleClass().remove("deckCard");
					cardElement.getStyleClass().add("deckGear");
				}
				
				playerDeckElement.getChildren().add(cardElement);
				
				// Set the rotation of the cardPanel depending on which cardPanel it is
				// First cardPanel will be rotated by max amount (ROTATION_PER_CARD * playerDeck.size() - 1)
				// Last cardPanel will not be rotated at all
				// This is done to keep the z-axis in check
				cardElement.setRotate((playerDeck.size() - i - 1) * ROTATION_PER_CARD);
				cardElement.setTranslateX(((playerDeck.size() - 1) * 3 / 4d - i) * ROTATION_PER_CARD * 4);      
			}
		} catch (IOException e) {
            System.out.println("[ERROR] Error loading player deck");
        }
    }
    
    public void createOpponentDeck() {
        // Clear the opponent deck
        opponentDeckElement.getChildren().clear();
        ArrayList<Deckable> opponentDeck = opponent.getDeck();
        try {
			for (int i = 0; i < opponentDeck.size(); i++) {
				Deckable currentCard = opponentDeck.get(i);
				
				VBox cardElement = null;
				
				if (currentCard instanceof Card) {
					cardElement = FXMLLoader.load(getClass().getResource("EnemyCardPanel.fxml"));
				} else if (currentCard instanceof Gear) {
					cardElement = FXMLLoader.load(getClass().getResource("EnemyGearPanel.fxml"));
				} 
				
				opponentDeckElement.getChildren().add(cardElement);
				
				/* --------------------------
				   SETTING OF CARD ATTRIBUTES
				   -------------------------- */
				// Sets rotation of cardPanel
				// Same principle as the createPlayerDeck() function
				cardElement.setRotate((opponentDeck.size() - i - 1) * -ROTATION_PER_CARD);
				cardElement.setTranslateX(((opponentDeck.size() - 1) * 3 / 4d - i) * -ROTATION_PER_CARD * 4);         
			}
		} catch (IOException e) {
            System.out.println("[ERROR] Error loading opponent deck");
        }
    }

	@FXML
	private void menuIconHoverEnter(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!isScreenActive) return;

		Node menuButton = (Node) event.getSource();

		ScaleTransition menuScale = new ScaleTransition();
		menuScale.setFromX(1);
		menuScale.setFromY(1);
		menuScale.setToX(1.1);
		menuScale.setToY(1.1);
		menuScale.setDuration(Duration.millis(100));
		menuScale.setNode(menuButton);
		menuScale.play();
	}

	@FXML
	private void menuIconHoverExit(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!isScreenActive) return;

		Node menuButton = (Node) event.getSource();

		ScaleTransition menuScale = new ScaleTransition();
		menuScale.setFromX(1.1);
		menuScale.setFromY(1.1);
		menuScale.setToX(1);
		menuScale.setToY(1);
		menuScale.setDuration(Duration.millis(100));
		menuScale.setNode(menuButton);
		menuScale.play();
	}

    @FXML
    private void slotHoverExit(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!isScreenActive) return;

        if (borderRedFlashing.getStatus() == Status.STOPPED) {
            ((AnchorPane) event.getSource()).getStyleClass().remove("selectedSlot");
        }
    }

	@FXML
	public void toggleMenu(MouseEvent event) {
		// If the menuButton was not open 
		if (menu.disableProperty().get()) {
			isScreenActive = false;	

			menu.setDisable(false);
			menu.setOpacity(1);

			for (Node child : root.getChildren()) {
				if (child != menu) {
					child.setOpacity(0.5);
				}
			}

			songPlayer.setVolume(0.2);
		// If the menuButton was open
		} else {
			isScreenActive = true;

			menu.setDisable(true);
			menu.setOpacity(0);

			for (Node child : root.getChildren()) {
				if (child != menu) {
					child.setOpacity(1);
				}
			}

			songPlayer.setVolume(musicVolume);
		}
	}

	@FXML
	private void onMouseHoverExit(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!isScreenActive) return;

		Node button = (Node) event.getSource();

		ScaleTransition buttonScale = new ScaleTransition();
		buttonScale.setFromX(1.1);
		buttonScale.setFromY(1.1);
		buttonScale.setToX(1);
		buttonScale.setToY(1);
		buttonScale.setDuration(Duration.millis(100));
		buttonScale.setNode(button);
		buttonScale.play();
	}

	@FXML
	private void onMouseHoverEnter(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!isScreenActive) return;

		Node button = (Node) event.getSource();

		ScaleTransition buttonScale = new ScaleTransition();
		buttonScale.setFromX(1);
		buttonScale.setFromY(1);
		buttonScale.setToX(1.1);
		buttonScale.setToY(1.1);
		buttonScale.setDuration(Duration.millis(100));
		buttonScale.setNode(button);
		buttonScale.play();
	}

	@FXML
	private void backToMenu(MouseEvent event) {
		songPlayer.setVolume(0);
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.MapScreen);
	}

	@FXML
	private void restartGame(MouseEvent event) {
		songPlayer.setVolume(0);
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.BattleScreen);
	}

    @FXML
    private void slotHoverEnter(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (!isScreenActive) return;

        if (borderRedFlashing.getStatus() == Status.STOPPED) {
            ((AnchorPane) event.getSource()).getStyleClass().add("selectedSlot");
        }
    }
	
	/**
	 * Function that both updates the orientation of the cards AND makes an animation for it.
	 */
	private void updateCardOrientation() {
		for (int i = 0; i < playerDeckElement.getChildren().size(); i++) {
			Node cardPanel = playerDeckElement.getChildren().get(i);

			RotateTransition cardRotate = new RotateTransition();
			cardRotate.setToAngle((player.getDeck().size() - i - 1) * ROTATION_PER_CARD);
			cardRotate.setDuration(Duration.millis(200));
			cardRotate.setNode(cardPanel);
			cardRotate.play();

			TranslateTransition cardMove = new TranslateTransition();
			cardMove.setToX(((player.getDeck().size()) * 3 / 4d - i) * ROTATION_PER_CARD * 4);
			cardMove.setDuration(Duration.millis(200));
			cardMove.setNode(cardPanel);
			cardMove.play();
		}
	}
    
    public void setToSlot(MouseEvent event, CardPanelController c) {
        double[] mousePos = {event.getSceneX(), event.getSceneY()};
        
        // Go through all of the slots and check if the mouse is over that one
        for (Node slot : playerPlayingField.getChildren()) {
            AnchorPane playerSlot = (AnchorPane) slot;
            Bounds slotBounds = playerSlot.localToScene(playerSlot.getBoundsInLocal());

            // If the mouse is over a slot when it is released,
            // drop the cardPanel!
            if (slotBounds.contains(mousePos[0], mousePos[1])) {
				// If the slot is occupied, play shaking animation	
				if (!playerSlot.getChildren().isEmpty()) {
					borderRedFlashing.setNode(playerSlot);
					borderRedFlashing.play();

					slotInvalid.setNode(playerSlot);
					slotInvalid.play();

					Media invalidSFX = new Media(getClass().getResource("/spacedeck/audio/invalidCard.wav").toExternalForm());
					MediaPlayer sfxPlayer = new MediaPlayer(invalidSFX);
					sfxPlayer.play();
					return;
				}

                // If the current slot is empty AND the fuel is sufficient
				Deckable card = c.getCard();

				// CHECK FIRST whether the card can be drawn or not
				// If insufficient fuel, make fuel text flash red
				// If card already active, return early
				try {
					player.drawCard((Card) c.getCard(), playerPlayingField.getChildren().indexOf(playerSlot));
				} catch (InsufficientFuelException e) {
					RedFlashingTransition textRed = new RedFlashingTransition("insufficientFuel");
					textRed.setDuration(Duration.millis(100));
					textRed.setCycleCount(3);
					textRed.setNode(playerFuelCount);
					textRed.play();

					Media invalidSFX = new Media(getClass().getResource("/spacedeck/audio/invalidCard.wav").toExternalForm());
					MediaPlayer sfxPlayer = new MediaPlayer(invalidSFX);
					sfxPlayer.play();
					return;
				} catch (CardAlreadyActiveException e) {
					System.out.println("Cannot add already active card.");
					return;
				}

				// Make a new slotPanel and put cardPanel info there
				// If there was an error, return early so that code after won't be executed
				FXMLLoader slotPanelLoader = new FXMLLoader(getClass().getResource("SlotPanel.fxml"));
				BorderPane slotPanel;
				try {
					slotPanel = slotPanelLoader.load();
				}  catch (IOException e) {
					System.out.println("[ERROR] IOException while loading slots");
					return;
				}
				
				SlotPanelController slotController = slotPanelLoader.getController();

				// Card attributes
				slotController.setCardAttack(((Card) card).getAttack());                    
				slotController.setCardHealth(((Card) card).getHealth());
				Image cardIcon = new Image(getClass().getResourceAsStream("/spacedeck/media/" + ((Card) card).getIcon()));
				slotController.setCardIcon(cardIcon);

				playerSlot.getChildren().add(slotPanel);

				// Destroy the cardPanel
				c.deinitialize();
				playerDeckElement.getChildren().remove(c.getSceneCard());

				// Move rotate all other cards to the right
				updateCardOrientation();

				// Animation
				takeSlot.setNode(playerSlot);
				takeSlot.play();

				Media deploySFX = new Media(getClass().getResource("/spacedeck/audio/deployCard.wav").toExternalForm());
				MediaPlayer sfxPlayer = new MediaPlayer(deploySFX);
				sfxPlayer.setVolume(0.3);
				sfxPlayer.play();
			}
        }
        
        updateFuel();
    }
	
	/**
	 * Function that gets a card from the stack and places it in the player's deck
	 */
    public void getCardFromStack(MouseEvent event) {
		ObservableList<Node> cardList = cardStackElement.getChildren();	   
		Node leadingCard = cardList.get(cardList.size() - 1);
		Bounds origin = leadingCard.localToScene(leadingCard.getBoundsInLocal());	
	
		Bounds cardStackDestination;
		
		if (playerDeckElement.getChildren().size() - 1 > 0) {
			Node destinationCard = playerDeckElement.getChildren().get(playerDeckElement.getChildren().size() - 1);
			cardStackDestination = destinationCard.localToScene(destinationCard.getBoundsInLocal());
		} else {
			// DEFAULT CARD STACK DESTINATION
			// Might not work if the window is resized
			// For when the deck is empty
			cardStackDestination = new BoundingBox(424, 400, 0, 0);	
		}
		FXMLLoader newCardLoader = new FXMLLoader(getClass().getResource("CardPanel.fxml"));	
		
		Node newCard;

		// If the card won't load, print an error and early return
		try {
			newCard = newCardLoader.load();
		} catch (IOException e) {
			System.out.println("[ERROR] Error loading random card from card stack");
			return;
		}

		CardPanelController newCardController = newCardLoader.getController();
		
		newCardController.setCard(Card.getRandomCard());

		try {
			player.addCard(newCardController.getCard());
		} catch (FullDeckException e) {
			return;
		}

		newCardController.setSceneController(this);
		newCardController.updateAttributes();
		
		addLeadingCardToDeck();
		
		System.out.println(cardStackDestination.getMinX());
		System.out.println(cardStackDestination.getMinY());
	
		CardStackTransition slideToDeck = new CardStackTransition();	
		slideToDeck.setNode(leadingCard);
		slideToDeck.setNewNode(newCard);
		slideToDeck.setDeckable(newCardController.getCard());
		slideToDeck.setByX(cardStackDestination.getMinX() - origin.getMinX());
		slideToDeck.setByY(cardStackDestination.getMinY() - origin.getMinY());
		slideToDeck.setDuration(Duration.millis(300));
		
		leadingCard.setLayoutX(newCard.getLayoutX());
		leadingCard.setLayoutY(newCard.getLayoutY());
		
		slideToDeck.setSceneController(this);
		slideToDeck.play();
    }
    
	private void addLeadingCardToDeck() {
		ObservableList<Node> cardList = cardStackElement.getChildren();	   
		Node leadingCard = cardList.get(cardList.size() - 1);
		
		// Set animations and card position 
		cardList.remove(leadingCard);
		playerDeckElement.getChildren().add(leadingCard);
		
		updateCardOrientation();	
	}
	
    private void updateFuel() {
        opponentFuelCount.setText(Integer.toString(opponent.getFuel()));
        playerFuelCount.setText(Integer.toString(player.getFuel()));
    }
   	
	// ATTRIBUTE SETTERS FOR OTHER FUNCTIONS

    public void setIsDraggingCard(boolean d) {
        isDraggingCard = d;
    }

	public boolean getIsScreenActive() {
		return isScreenActive;
	}

	public void setIsScreenActive(boolean isScreenActive) {
		this.isScreenActive = isScreenActive;
	}
}