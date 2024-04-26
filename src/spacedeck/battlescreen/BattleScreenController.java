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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.util.Duration;
import spacedeck.SpaceDeck;
import spacedeck.exceptions.CardAlreadyActiveException;
import spacedeck.exceptions.FullDeckException;
import spacedeck.exceptions.InsufficientFuelException;
import spacedeck.model.OpponentMove;

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
	@FXML
	private AnchorPane turnButton;
	
    private final int CARDS_IN_CARD_STACK = 10;
    private final double ROTATION_PER_CARD = -10;
	private LinearGradient INITIAL_TURN_BUTTON_GRADIENT;
	private LinearGradient UPDATED_TURN_BUTTON_GRADIENT;    
    private Player player;
    private Opponent opponent;
	private Character turn;
    private boolean isDraggingCard;
	private boolean isPaused;
	private double musicVolume = 0.3;
    
    // TRANSITIONS
    private TranslateTransition slotInvalid;
    private ScaleTransition takeSlot;
    private RedFlashingTransition borderRedFlashing;
    private MediaPlayer songPlayer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		isPaused = false;

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
            opponent = new Opponent("Bob Ross", 20, 3, AILevel.RANDOM);
//            for (int i = 0; i < 4; i++) {
//           		opponent.addCard(Card.getRandomCard());
//            }
        } catch (FullDeckException e) {
            System.out.println("[ERROR] FullDeckException");
        }
        
		// Create all cardPanel decks and stacks
		createPlayerDeck();
		createOpponentDeck();
		createCardStack();

        // Set the turn indicator
		turn = player;
        turnIndicator.setText("End Turn");

        opponentFuelCount.setText(Integer.toString(opponent.getFuel()));
        playerFuelCount.setText(Integer.toString(player.getFuel()));
        
        // Play music
        Media battleMusic = new Media(getClass().getResource("/spacedeck/audio/TEMP_battle_music.mp3").toExternalForm());
        songPlayer = new MediaPlayer(battleMusic);
        songPlayer.setVolume(musicVolume);
        songPlayer.setAutoPlay(true);
        songPlayer.play();

		this.INITIAL_TURN_BUTTON_GRADIENT = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop(0, new Color(52d/255, 41d/255, 102d/255, 1)),
				new Stop(1, new Color(42d/255d, 78d/255, 135d/255, 1))
		);
		this.UPDATED_TURN_BUTTON_GRADIENT = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
			new Stop(0, new Color(0, 232d/255, 255d/255, 1)),
			new Stop(1, new Color(52d/255, 41d/255, 102d/255, 1))
		);
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
		if (isPaused) return;

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
		if (isPaused) return;

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
		if (isPaused) return;
		if (turn != player) return;

        if (borderRedFlashing.getStatus() == Status.STOPPED) {
            ((AnchorPane) event.getSource()).getStyleClass().remove("selectedSlot");
        }
    }

	@FXML
	public void toggleMenu(MouseEvent event) {
		// If the menuButton was not open 
		if (menu.disableProperty().get()) {
			isPaused = true;	

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
			isPaused = false;

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
		if (isPaused) return;
		if (turn != player) return;

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
		if (isPaused) return;
		if (turn != player) return;

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
		if (!isPaused) return;

		isPaused = false;

		songPlayer.setVolume(0);
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.MapScreen);
	}

	@FXML
	private void restartGame(MouseEvent event) {
		if (!isPaused) return;

		isPaused = false;

		songPlayer.setVolume(0);
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.BattleScreen);
	}

    @FXML
    private void slotHoverEnter(MouseEvent event) {
		// If the screen is not active, don't do anything
		if (isPaused) return;
		if (turn != player) return;

        if (borderRedFlashing.getStatus() == Status.STOPPED) {
            ((AnchorPane) event.getSource()).getStyleClass().add("selectedSlot");
        }
    }
	
	/**
	 * Function that both updates the orientation of the cards AND makes an animation for it.
	 */
	private void updateCardOrientation(Character ch) {
		AnchorPane deckElement = null;
		if (ch == player) {
			deckElement = playerDeckElement;
		} else {
			deckElement = opponentDeckElement;
		}
		for (int i = 0; i < deckElement.getChildren().size(); i++) {
			Node cardPanel = deckElement.getChildren().get(i);

			RotateTransition cardRotate = new RotateTransition();
			cardRotate.setToAngle((ch.getDeck().size() - i - 1) * ROTATION_PER_CARD);
			cardRotate.setDuration(Duration.millis(200));
			cardRotate.setNode(cardPanel);
			cardRotate.play();

			TranslateTransition cardMove = new TranslateTransition();
			cardMove.setToX(((ch.getDeck().size()) * 3 / 4d - i) * ROTATION_PER_CARD * 4);
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
				updateCardOrientation(player);

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
    public void getCardFromStack(MouseEvent event, Character character) {
		ObservableList<Node> cardList = cardStackElement.getChildren();	   
		Node leadingCard = cardList.get(cardList.size() - 1);
		Bounds origin = leadingCard.localToScene(leadingCard.getBoundsInLocal());	
		Bounds cardStackDestination;

		if (character == player) {
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
			
			addLeadingCardToDeck(player);
			
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
		} else {
			// OPPONENT STUFF
			if (opponentDeckElement.getChildren().size() - 1 > 0) {
				Node destinationCard = opponentDeckElement.getChildren().get(opponentDeckElement.getChildren().size() - 1);
				cardStackDestination = destinationCard.localToScene(destinationCard.getBoundsInLocal());
			} else {
				// DEFAULT CARD STACK DESTINATION
				// Might not work if the window is resized
				// For when the deck is empty
				cardStackDestination = new BoundingBox(424, 400, 0, 0);	
			}

			try {
				Node newCard = FXMLLoader.load(getClass().getResource("/spacedeck/battlescreen/EnemyCardPanel.fxml"));
			
				opponentDeckElement.getChildren().add(newCard);
				cardStackElement.getChildren().remove(leadingCard);
				OpponentDrawTransition drawCard = new OpponentDrawTransition();

				drawCard.setNode(newCard);
				drawCard.setByX(cardStackDestination.getMinX() - origin.getMinX());
				drawCard.setByY(cardStackDestination.getMinY() - origin.getMinY());
				drawCard.setDuration(Duration.millis(300));

				drawCard.play();

				drawCard.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						updateCardOrientation(character);
					}	
				});
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
    }
    
	private void addLeadingCardToDeck(Character ch) { 
		ObservableList<Node> cardList = cardStackElement.getChildren();	   
		Node leadingCard = cardList.get(cardList.size() - 1);
		
		// Set animations and card position 
		cardList.remove(leadingCard);
		if (ch == player) {
			playerDeckElement.getChildren().add(leadingCard);
		} else {
			opponentDeckElement.getChildren().add(leadingCard);
		}
		
		updateCardOrientation(ch);	
	}
	
    private void updateFuel() {
        opponentFuelCount.setText(Integer.toString(opponent.getFuel()));
        playerFuelCount.setText(Integer.toString(player.getFuel()));
    }

	// ATTRIBUTE SETTERS FOR OTHER FUNCTIONS
	public void setPlayer(Player p) {
		this.player = p;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setOpponent(Opponent o) {
		this.opponent = o;
	}

    public void setIsDraggingCard(boolean d) {
        isDraggingCard = d;
    }

	public boolean getIsScreenActive() {
		return !isPaused;
	}

	public void setIsScreenActive(boolean isPaused) {
		this.isPaused = !isPaused;
	}

	public boolean isPlayerTurn() {
		return this.turn == this.player;
	}

	@FXML
	private void onTurnButtonMouseExit(MouseEvent event) {
		if (turn != player) return;

		ScaleTransition buttonScale = new ScaleTransition();
		buttonScale.setDuration(Duration.millis(200));
		buttonScale.setFromX(1.1);
		buttonScale.setFromY(1.1);
		buttonScale.setToX(1);
		buttonScale.setToY(1);
		buttonScale.setNode(turnButton);
		buttonScale.play();

		GradientTransition gradientTrans = new GradientTransition();
		gradientTrans.setOriginalGradient(this.UPDATED_TURN_BUTTON_GRADIENT);
		gradientTrans.setUpdatedGradient(this.INITIAL_TURN_BUTTON_GRADIENT);
		gradientTrans.setDuration(Duration.millis(100));
		gradientTrans.setNode(turnButton);
		gradientTrans.play();
	}

	@FXML
	private void onTurnButtonMouseEnter(MouseEvent event) {
		if (turn != player) return;

		ScaleTransition buttonScale = new ScaleTransition();
		buttonScale.setDuration(Duration.millis(200));
		buttonScale.setFromX(1);
		buttonScale.setFromY(1);
		buttonScale.setToX(1.1);
		buttonScale.setToY(1.1);
		buttonScale.setNode(turnButton);
		buttonScale.play();

		GradientTransition gradientTrans = new GradientTransition();
		gradientTrans.setOriginalGradient(this.INITIAL_TURN_BUTTON_GRADIENT);
		gradientTrans.setUpdatedGradient(this.UPDATED_TURN_BUTTON_GRADIENT);
		gradientTrans.setDuration(Duration.millis(100));
		gradientTrans.setNode(turnButton);
		gradientTrans.play();
	}

	@FXML
	private void onTurnButtonMouseClicked(MouseEvent event) {
		if (turn != player) return;
		endTurn();
	}
	
	/**
	 * Ends the turn of the player currently playing.
	 * <br>
	 * <br>
	 * <h3 style="margin: 0px, padding: 0px">
	 * If it is the {@code player's} turn: 
	 * </h3>
	 * <p>
	 * Switch turn to opponent.
	 * Make the button transparent.
	 * Also disable controls for the player (moving cards, selecting slots, getting from stack, etc.)
	 * Execute enemy AI.
	 * Once AI finishes making choice, execute endTurn again to switch control to player.
	 * </p>
	 * <br>
	 * <br>
	 * <h3 style="margin: 0px, padding: 0px">
	 * If it is the {@code opponent's} turn: 
	 * </h3>
	 * <p>
	 * Switch turn to player.
	 * Enable controls for the player.
	 * Restore the button's transparency.
	 * </p>
	 * @param event 
	 */	
	private void endTurn() {
		if (turn == player) {
			turn = opponent;

			turnButton.setBackground(Background.EMPTY);
			turnIndicator.setOpacity(1);

			ArrayList<OpponentMove> moveList = opponent.decideMoves();
			for (OpponentMove move : moveList) {
				System.out.println(move.getType().name());
			}
			executeMoves(moveList);
			
			endTurn();
		} else {
			turn = player;
			turnButton.setBackground(new Background(new BackgroundFill(this.INITIAL_TURN_BUTTON_GRADIENT, new CornerRadii(20), Insets.EMPTY)));
			turnButton.setOpacity(1);
		}
	}

	private void executeMoves(ArrayList<OpponentMove> moves) {
		moves.forEach((move) -> {
			switch (move.getType()) {
				case SKIP:
					break;
				case DRAW:
					getCardFromStack(null, opponent);
					break;

			}
		});
	}

}