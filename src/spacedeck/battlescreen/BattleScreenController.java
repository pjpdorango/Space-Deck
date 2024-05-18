/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package spacedeck.battlescreen;

import spacedeck.util.FlashingTransition;
import spacedeck.util.GradientTransition;
import spacedeck.util.OpponentDrawTransition;
import spacedeck.util.CardStackTransition;
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
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import spacedeck.SpaceDeck;
import spacedeck.exceptions.CardAlreadyActiveException;
import spacedeck.exceptions.CardNotInDeckException;
import spacedeck.exceptions.FullDeckException;
import spacedeck.exceptions.InsufficientFuelException;
import spacedeck.model.OpponentMove;
import spacedeck.util.Playable;

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
	private HBox opponentPlayingField;
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

	// The default position of the destination for when the stack card is drawn.
	private Bounds PLAYER_DECK_POSITION = null;
	private Bounds ENEMY_DECK_POSITION = null;
	
    private final int CARDS_IN_CARD_STACK = 10;
    private final double ROTATION_PER_CARD = -10;
	private LinearGradient INITIAL_TURN_BUTTON_GRADIENT;
	private LinearGradient UPDATED_TURN_BUTTON_GRADIENT;    
	private LinearGradient INITIAL_GLOW_GRADIENT;
	private LinearGradient UPDATED_GLOW_GRADIENT;    
    private Player player;
    private Opponent opponent;
	private Character turn;
	private int selectedPlayerSlot;
	private boolean playerDrawnCard;
    private boolean isDraggingCard;
	private boolean isPaused;
	private double musicVolume = 0.3;
	private double sfxVolume = 0.3;
	private double scale;
	private IdentityHashMap<AnchorPane, SlotPanelController> slotControllers = new IdentityHashMap<>(); 
	private Set<AnchorPane> alreadyAttacked = Collections.newSetFromMap(new IdentityHashMap<>()); 
    
    // TRANSITIONS
    private TranslateTransition slotInvalid;
    private FlashingTransition borderRedFlashing;
    private MediaPlayer songPlayer;
	@FXML
	private Rectangle enemyAttackGlow;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		isPaused = false;
		selectedPlayerSlot = -1;

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
        
        borderRedFlashing = new FlashingTransition("invalidSlot");
        borderRedFlashing.setCycleCount(3);
        borderRedFlashing.setDuration(new Duration(100));
        
		musicVolume *= (double) SpaceDeck.getSetting("musicVolume") / 100 * (double) SpaceDeck.getSetting("masterVolume") / 100;
		sfxVolume *= (double) SpaceDeck.getSetting("sfxVolume") / 100 * (double) SpaceDeck.getSetting("masterVolume") / 100;
        
        try {
            // Instantiate Player
            player = new Player("PJ", 20, 1);
            for (int i = 0; i < 1; i++) {
				player.addCard(Card.getRandomCard());
            }
            
            // Instantiate Opponent
            opponent = new Opponent("Bob Ross", 20, 3, AILevel.ADVANCED);
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

		this.INITIAL_GLOW_GRADIENT = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop(0, new Color(1, 0.1, 0, 1)),
				new Stop(1, new Color(1, 1, 1, 0))
		);
		this.UPDATED_GLOW_GRADIENT = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop(0, new Color(1, 204d/255, 0, 1)),
				new Stop(1, new Color(1, 1, 1, 0))
		);

		this.PLAYER_DECK_POSITION = new BoundingBox(424, 400, 0, 0);
		this.ENEMY_DECK_POSITION = new BoundingBox(428, 5, 0, 0);
		scale = (double) SpaceDeck.getSetting("scale");
		playerDrawnCard = false;
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

			songPlayer.setVolume(musicVolume / 2);
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

	@FXML
	private void onTurnButtonMouseExit(MouseEvent event) {
		if (isPaused) return;
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
		if (isPaused) return;
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
		if (isPaused) return;
		if (turn != player) return;
		endTurn();
	}

	@FXML
	private void enemySlotClicked(MouseEvent event) {
		if (isPaused) return;
		if (selectedPlayerSlot == -1) return;
		AnchorPane selectedSlot = (AnchorPane) event.getSource();
		if (selectedSlot.getChildren().isEmpty()) return;
		
		clearHighlights();
		AnchorPane playerSlot = (AnchorPane) playerPlayingField.getChildren().get(selectedPlayerSlot);

		alreadyAttacked.add(playerSlot);

		playEvents(attack(playerSlot, selectedSlot));

		selectedPlayerSlot = -1;
	}

	private List<Object> attack(AnchorPane selectedSlot, AnchorPane targetSlot) {
		ArrayList<Object> tier = new ArrayList<>();

		TranslateTransition move = new TranslateTransition();
		int byY = 20;
		if (turn == player) {
			byY *= -1;
		}

		int selectedCardIndex, targetCardIndex;
		if (turn == player) {
			selectedCardIndex = playerPlayingField.getChildren().indexOf(selectedSlot);
			targetCardIndex = opponentPlayingField.getChildren().indexOf(targetSlot);
		} else {
			selectedCardIndex = opponentPlayingField.getChildren().indexOf(selectedSlot);
			targetCardIndex = playerPlayingField.getChildren().indexOf(targetSlot);
		}

		int byX = 0;
		if (selectedCardIndex < targetCardIndex) {
			byX = 10;
		} else if (selectedCardIndex > targetCardIndex) {
			byX = -10;
		}

		move.setByX(byX);
		move.setByY(byY);
		move.setDuration(Duration.millis(100));
		move.setCycleCount(2);
		move.setAutoReverse(true);
		move.setNode(selectedSlot);
		tier.add(move);

		// BUG: -1 selectedCardIndex somehow????
		Card selectedCard = turn.getPlayingField()[selectedCardIndex];

		Character targetCharacter = turn == player ? opponent : player;
		Card targetCard = targetCharacter.getPlayingField()[targetCardIndex];

		FlashingTransition transition = new FlashingTransition("invincibilityFrame");	
		transition.setDuration(Duration.millis(100));
		transition.setCycleCount(3);
		transition.setNode(targetSlot);
		tier.add(transition);

		// Update data on slots
		tier.add((Playable) () -> {
			selectedCard.attack(targetCard);
			updateSlotData(targetSlot);
			updateFuel();
		});

		Media attackSfx = new Media(getClass().getResource("/spacedeck/audio/attack.mp3").toExternalForm());
		MediaPlayer sfxPlayer = new MediaPlayer(attackSfx);
		sfxPlayer.setVolume(sfxVolume);
		tier.add(sfxPlayer);

		tier.add((Playable) () -> {
			if (targetCard.getHealth() <= 0) {
				try {
					targetCharacter.undrawCard(targetCardIndex);
				} catch (CardNotInDeckException e) {
					return;
				}

				// No errors -> Die 
				Media deathSFX = new Media(getClass().getResource("/spacedeck/audio/death.mp3").toExternalForm());
				MediaPlayer deathSFXPlayer = new MediaPlayer(deathSFX);
				deathSFXPlayer.setVolume(sfxVolume * 4);
				deathSFXPlayer.play();
				
				targetSlot.getStyleClass().add("invalidSlot");
				EventHandler<ActionEvent> oldOnFinished = transition.getOnFinished();
				transition.setOnFinished(e -> {
					if (oldOnFinished != null) oldOnFinished.handle(e);

					targetSlot.getChildren().clear();
					targetSlot.getStyleClass().remove("invalidSlot");
					clearHighlights();
				});
			}
		});

		return tier;
	}

	private void updateSlotData(AnchorPane slot) {
		SlotPanelController slotController = slotControllers.get(slot);
		slotController.updateData();
	}

	@FXML
	private void playerSlotClicked(MouseEvent event) {
		AnchorPane newSlot = (AnchorPane) event.getSource();
		int index = playerPlayingField.getChildren().indexOf(newSlot);

		if (newSlot.getChildren().isEmpty()) return;
		if (alreadyAttacked.contains(newSlot)) return;

		if (selectedPlayerSlot != -1) {
			Node oldSlot = playerPlayingField.getChildren().get(selectedPlayerSlot);
			oldSlot.getStyleClass().remove("currentSlot");
		}

		if (selectedPlayerSlot == index) {
			clearHighlights();
			selectedPlayerSlot = -1;
		} else {
			selectedPlayerSlot = index;
			newSlot.getStyleClass().add("currentSlot");

			// Light up all available enemy slots
			highlightEnemyCells(index);
		}
	}

	private void highlightEnemyCells(int slotIndex) {
		for (Node slot : opponentPlayingField.getChildren()) {
			AnchorPane opponentSlot	= (AnchorPane) slot;
			if (!opponentSlot.getChildren().isEmpty()) {
				opponentSlot.getStyleClass().add("targetSlot");
			}
		}

		if (opponent.getPlayingField()[slotIndex] == null) {
			enemyAttackGlow.setOpacity(0.5);
		} else {
			enemyAttackGlow.setOpacity(0);
		}
	}

	private void clearHighlights() {
		if (selectedPlayerSlot == -1) return;

		for (Node slot : opponentPlayingField.getChildren()) {
			slot.getStyleClass().remove("targetSlot");
		}

		AnchorPane selectedSlot = (AnchorPane) playerPlayingField.getChildren().get(selectedPlayerSlot);
		selectedSlot.getStyleClass().remove("currentSlot");

		enemyAttackGlow.setOpacity(0);
	}

	@FXML
	private void opponentGlowEnter(MouseEvent event) {
		GradientTransition gradTrans = new GradientTransition();

		System.out.println("ha");
		
		gradTrans.setOriginalGradient(INITIAL_GLOW_GRADIENT);
		gradTrans.setUpdatedGradient(UPDATED_GLOW_GRADIENT);
		gradTrans.setDuration(Duration.millis(50));
		gradTrans.setNode(enemyAttackGlow);
		gradTrans.play();
	}

	@FXML
	private void opponentGlowExit(MouseEvent event) {
		GradientTransition gradTrans = new GradientTransition();
		
		System.out.println("ah");
		gradTrans.setOriginalGradient(UPDATED_GLOW_GRADIENT);
		gradTrans.setUpdatedGradient(INITIAL_GLOW_GRADIENT);
		gradTrans.setDuration(Duration.millis(50));
		gradTrans.setNode(enemyAttackGlow);
		gradTrans.play();
	}
	
	/**
	 * Function that both updates the orientation of the cards AND makes an animation for it.
	 */
	private void updateCardOrientation(Character ch) {
		AnchorPane deckElement;
		Bounds leadingCardPos;

		// If the character to update the orientation is the player, the deck element is the player's
		// and the leading card pos is the corresponding deck position
		// HOWEVER, player doesn't use translate for their animations, so setting leadingCardPos
		// to origin so that it doesn't interfere with animation
		if (ch == player) {
			deckElement = playerDeckElement;
			leadingCardPos = new BoundingBox(0, 0, 0, 0);
		} else {
			deckElement = opponentDeckElement;
			leadingCardPos = this.ENEMY_DECK_POSITION;
		}

		for (int i = 0; i < deckElement.getChildren().size(); i++) {
			Node cardPanel = deckElement.getChildren().get(i);

			RotateTransition cardRotate = new RotateTransition();
			cardRotate.setToAngle((ch.getDeck().size() - i - 1) * ROTATION_PER_CARD);
			cardRotate.setDuration(Duration.millis(200));
			cardRotate.setNode(cardPanel);
			cardRotate.play();

			TranslateTransition cardMove = new TranslateTransition();
			cardMove.setToX(leadingCardPos.getMinX() + ((ch.getDeck().size()) * 3 / 4d - i) * ROTATION_PER_CARD * 4);
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
					sfxPlayer.setVolume(sfxVolume);
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
					FlashingTransition textRed = new FlashingTransition("insufficientFuel");
					textRed.setDuration(Duration.millis(100));
					textRed.setCycleCount(3);
					textRed.setNode(playerFuelCount);
					textRed.play();

					Media invalidSFX = new Media(getClass().getResource("/spacedeck/audio/invalidCard.wav").toExternalForm());
					MediaPlayer sfxPlayer = new MediaPlayer(invalidSFX);
					sfxPlayer.setVolume(sfxVolume);
					sfxPlayer.play();
					return;
				} catch (CardAlreadyActiveException e) {
					System.out.println("Cannot add already active card.");
					return;
				}
				
				// Move rotate all other cards to the right
				updateCardOrientation(player);

				playEvents(popNodeToSlot(playerSlot, c.getSceneCard(), (Card) card));
			}
        }
        
    }
	
	/**
	 * Function that gets a card from the stack and places it in the player's deck
	 * @param event
	 */
	public Queue<List<Object>> getCardFromStack(MouseEvent event, Character character) {
		Queue<List<Object>> transitionsList = new LinkedList();
		ArrayList<Object> tier = new ArrayList<>();	
		transitionsList.add(tier);

		// For both player and opponent, these variables are used
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
				cardStackDestination = this.PLAYER_DECK_POSITION;	
			}
			FXMLLoader newCardLoader = new FXMLLoader(getClass().getResource("CardPanel.fxml"));	
			
			Node newCard;

			// If the card won't load, print an error and early return
			try {
				newCard = newCardLoader.load();
			} catch (IOException e) {
				System.out.println("[ERROR] Error loading random card from card stack");
				return null;
			}

			CardPanelController newCardController = newCardLoader.getController();
			
			newCardController.setCard(Card.getRandomCard());

			try {
				player.addCard(newCardController.getCard());
			} catch (FullDeckException e) {
				return null;
			}

			newCardController.setSceneController(this);
			newCardController.updateAttributes();
			
			addLeadingCardToDeck(player);
			
			CardStackTransition slideToDeck = new CardStackTransition();	
			slideToDeck.setNode(leadingCard);
			slideToDeck.setNewNode(newCard);
			slideToDeck.setScale(scale);
			slideToDeck.setDeckable(newCardController.getCard());
			slideToDeck.setByX((cardStackDestination.getMinX() - origin.getMinX()) / scale);
			slideToDeck.setByY((cardStackDestination.getMinY() - origin.getMinY()) / scale);
			slideToDeck.setDuration(Duration.millis(300));
			
			leadingCard.setLayoutX(newCard.getLayoutX());
			leadingCard.setLayoutY(newCard.getLayoutY());
			
			slideToDeck.setSceneController(this);
			slideToDeck.play();
		} else {
			// --------------
			// OPPONENT STUFF
			// --------------
			if (opponentDeckElement.getChildren().size() > 1) {
				Node destinationCard = opponentDeckElement.getChildren().get(opponentDeckElement.getChildren().size() - 1);
				cardStackDestination = destinationCard.localToScene(destinationCard.getBoundsInLocal());
			} else {
				// DEFAULT CARD STACK DESTINATION
				// Might not work if the window is resized
				// For when the deck is empty
				cardStackDestination = this.ENEMY_DECK_POSITION;	
			}

			addLeadingCardToDeck(opponent);

			OpponentDrawTransition drawCard = new OpponentDrawTransition();
			drawCard.setNode(leadingCard);
			drawCard.setOrig(new BoundingBox(cardStackDestination.getMinX() / scale, cardStackDestination.getMinY() / scale, 0, 0));
			drawCard.setByX((cardStackDestination.getMinX() - origin.getMinX())/scale);
			drawCard.setByY((cardStackDestination.getMinY() - origin.getMinY())/scale);
			drawCard.setDuration(Duration.millis(300));

			drawCard.play();

			List newTier = new ArrayList();
			transitionsList.add(newTier);
			newTier.add((Playable) () -> {
				updateCardOrientation(character);
			});

			tier.add(drawCard);
		}

		Media drawSFX = new Media(getClass().getResource("/spacedeck/audio/drawCard.mp3").toExternalForm());
		MediaPlayer sfxPlayer = new MediaPlayer(drawSFX);
		sfxPlayer.setVolume(sfxVolume * 3);
		tier.add(sfxPlayer);

		return transitionsList;
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
	 */	
	public void endTurn() {
		clearHighlights();

		if (turn == player) {
			turn = opponent;

			turnButton.setBackground(Background.EMPTY);
			turnIndicator.setOpacity(1);

			ArrayList<OpponentMove> moveList = opponent.decideMoves(player);
			executeMoves(moveList);
		} else {
			turn = player;
			turnButton.setBackground(new Background(new BackgroundFill(this.INITIAL_TURN_BUTTON_GRADIENT, new CornerRadii(20), Insets.EMPTY)));
			turnButton.setOpacity(1);
			playerDrawnCard = false;
			alreadyAttacked.clear();
		}
	}

	private Queue<List<Object>> opponentDrawCard(OpponentMove move) {
		Queue<List<Object>> t = getCardFromStack(null, opponent);
		opponent.getDeck().add(Card.getRandomCard());

		return t;
	}

	private Queue<List<Object>> opponentDeployCard(OpponentMove move) {
		if (opponent.getDeck().isEmpty()) {
			System.out.println("[ERROR] AI tried to deploy card with empty deck [opponentDeployCard()]");
			return null;
		}

		Card deployCard = move.getDeployCard();
		int targetSlot = move.getDeployTarget();

		try {
			opponent.drawCard(deployCard, targetSlot);
		} catch (InsufficientFuelException e) {
			System.out.println("[ERROR] AI tried to deploy card but has insufficient fuel");
			return null;
		} catch (CardAlreadyActiveException e) {
			System.out.println("[ERROR] Card deployed is already active");
			return null;
		}

		Node slot = opponentPlayingField.getChildren().get(targetSlot);
		Bounds slotBounds = slot.localToScene(slot.getBoundsInLocal());

		// The actual nodes these correspond to cards correspond to
		Node leadingCard = opponentDeckElement.getChildren().get(opponentDeckElement.getChildren().size() - 1);
		Bounds cardBounds = leadingCard.localToScene(leadingCard.getBoundsInLocal());
		
		Queue<List<Object>> allTransitions = new LinkedList();
		allTransitions.add(slideTransitionToNode(slotBounds, cardBounds, leadingCard));
		List<Object> tier = popNodeToSlot((AnchorPane) slot, leadingCard, deployCard);
		allTransitions.add(tier);

		return allTransitions;
	}

	private List<Object> slideTransitionToNode(Bounds slotBounds, Bounds cardBounds, Node card) {
		List<Object> tier = new ArrayList<>();

		TranslateTransition slide = new TranslateTransition();
		slide.setByX((slotBounds.getMinX() + slotBounds.getWidth() / 2 - cardBounds.getMinX() - cardBounds.getWidth() / 2) / scale);
		slide.setByY((slotBounds.getMinY() + slotBounds.getHeight() / 2 - cardBounds.getMinY() - cardBounds.getHeight() / 2) / scale);
		slide.setDuration(Duration.millis(400));
		slide.setNode(card);
		slide.setInterpolator(Interpolator.EASE_OUT);
		tier.add(slide);

		return tier;
	}

	private List<Object> popNodeToSlot(AnchorPane slot, Node card, Card cardEquiv) {
		List<Object> tier = new ArrayList<>();

		slot.getStyleClass().add("selectedSlot");
        ScaleTransition takeSlot = new ScaleTransition();
        takeSlot.setByX(0.1);
        takeSlot.setByY(0.1);
        takeSlot.setAutoReverse(true);
        takeSlot.setCycleCount(2);
        takeSlot.setDuration(new Duration(70));

		takeSlot.setNode(slot);
		tier.add(takeSlot);

		Media deploySFX = new Media(getClass().getResource("/spacedeck/audio/deployCard.wav").toExternalForm());
		MediaPlayer sfxPlayer = new MediaPlayer(deploySFX);
		sfxPlayer.setVolume(sfxVolume);
		tier.add(sfxPlayer);
		tier.add((Playable) () -> {
			((AnchorPane) card.getParent()).getChildren().remove(card);
			addCardToSlot(slot, cardEquiv);
			slot.getStyleClass().remove("selectedSlot");
		});

		return tier;
	}

	private void addCardToSlot(AnchorPane slot, Card card) {
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
		slotControllers.put(slot, slotController);
        updateFuel();

		// Card attributes
		slotController.setCard(card);

		slot.getChildren().add(slotPanel);
	}

	private void executeMoves(ArrayList<OpponentMove> moves) {
		/* 	
			A queue of every transition. Each "level" in the queue is a list of
		   	"events" to be played simultaneously.
		   	These events can be either a MediaPlayer (SFX), a Transition (movement),
			or an Animation (misc. movement).
			Any other type of Object will not be recognized by the execution queue.
		*/
		Queue<List<Object>> executionQueue = new LinkedList();

		moves.forEach((move) -> {
			switch (move.getType()) {
				case SKIP:
					endTurn();
					break;
				case DRAW:
					executionQueue.addAll(opponentDrawCard(move));
					break;
				case DEPLOY_CARD:
					executionQueue.addAll(opponentDeployCard(move));
					break;
				case ATTACK:
					AnchorPane opponentSlot = (AnchorPane) opponentPlayingField.getChildren().get(move.getAttacker());
					AnchorPane playerSlot = (AnchorPane) playerPlayingField.getChildren().get(move.getAttackTarget());
					executionQueue.add(attack(opponentSlot, playerSlot));
					break;
			}
		});

		/*
			-------------------
			DEBUG PURPOSES ONLY
			-------------------
		*/
		System.out.println("TRANSITIONS LIST: ");
		for (List<Object> list : executionQueue) {
			System.out.print("[TIER] ");

			for (Object event : list) {
				System.out.print(event.getClass().toString() + " ");
			}

			System.out.println("");
		}

		// Plays all of the transitions in the queue
		// Every transition in each "tier" or level in the queue is played simultaneously
		// Every tier will be played one after the other
		// This will be done by getting the leading transition on all tiers (w/ playEvents() & getLeadingTransition())
		// And then setting the onFinished of that to do its usual function AND play the next set of transitions
		// 
		Transition lastLeadingTransition = null;
		for (List<Object> transitions : executionQueue) {
			if (lastLeadingTransition == null) {
				playEvents(transitions);
				lastLeadingTransition = getLeadingTransition(transitions);
			} else {
				EventHandler<ActionEvent> oldOnFinished = lastLeadingTransition.getOnFinished();
				lastLeadingTransition.setOnFinished((e) -> {
					if (oldOnFinished != null) oldOnFinished.handle(e);
					System.out.println("DONE");
					playEvents(transitions);
				});
				Transition leadingTransition = getLeadingTransition(transitions);

				if (leadingTransition != null) {
					lastLeadingTransition = leadingTransition;
				}
			}
		}

		EventHandler<ActionEvent> oldOnFinished = lastLeadingTransition.getOnFinished();

		lastLeadingTransition.setOnFinished((e) -> {
			if (oldOnFinished != null) oldOnFinished.handle(e);
			System.out.println("Bruh");
			endTurn();
		});
	}

	private Transition getLeadingTransition(List<Object> events) {
		Transition leadingTransition = null;
		for (Object o : events) {
			if (o instanceof Transition) {
				Transition t = (Transition) o;

				if (leadingTransition == null) leadingTransition = t;
				else {
					double leadingTransitionTime = leadingTransition.getCycleDuration().toSeconds() * leadingTransition.getCycleCount();
					double currentTransitionTime = t.getCycleDuration().toSeconds() * t.getCycleCount();

					if (currentTransitionTime > leadingTransitionTime) {
						leadingTransition = t;
					}
				}
			}
		}

		return leadingTransition;
	}

	public void playEvents(List<Object> events) {
		for (Object o : events) {
			if (o instanceof MediaPlayer) {
				((MediaPlayer) o).play();
			} else if (o instanceof Transition) {
				((Transition) o).play();
			} else if (o instanceof Playable) {
				((Playable) o).play();
			}
		}
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

	public double getScreenScale() {
		return scale;
	}

	public void setIsScreenActive(boolean isPaused) {
		this.isPaused = !isPaused;
	}

	public boolean isPlayerTurn() {
		return this.turn == this.player;
	}

	public boolean hasPlayerDrawnCard() {
		return playerDrawnCard;
	}

	public void setPlayerDrawnCard(boolean playerDrawnCard) {
		this.playerDrawnCard = playerDrawnCard;
	}
}