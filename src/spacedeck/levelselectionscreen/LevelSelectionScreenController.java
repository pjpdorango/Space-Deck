package spacedeck.levelselectionscreen;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import spacedeck.model.Planet;
import spacedeck.model.Opponent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import spacedeck.SpaceDeck;

/**
 * FXML Controller class
 *
 * @author mika
 */
public class LevelSelectionScreenController implements Initializable {

	@FXML
	private AnchorPane levelContainer;

	private int selectedLevel;
	@FXML
	private ImageView battleIcon;
	private Image originalBattleIcon;
	private Image greyScaleBattleIcon;
	@FXML
	private ImageView planetImage;
	@FXML
	private Text planetName;
	@FXML
	private Text population;
	@FXML
	private Text diameter;
	@FXML
	private Text environment;
	@FXML
	private VBox championsList;
	@FXML
	private Text description;

	private boolean isScreenActive;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		isScreenActive = true;
		selectedLevel = -1;
		originalBattleIcon = battleIcon.getImage();
		greyScaleBattleIcon = makeGreyScale(battleIcon);
		disableBattle();
    }    

	@FXML
	private void buttonHoverEnter(MouseEvent event) {
		// Only do if screen is active
		if (!isScreenActive) return;

		ScaleTransition hover = new ScaleTransition();
		hover.setFromX(1);
		hover.setFromY(1);
		hover.setToX(1.1);
		hover.setToY(1.1);
		hover.setDuration(Duration.millis(100));
		hover.setNode((Node) event.getSource());
		hover.play();
	}

	@FXML
	private void buttonHoverExit(MouseEvent event) {
		// Only do if screen is active
		if (!isScreenActive) return;

		ScaleTransition hover = new ScaleTransition();
		hover.setFromX(1.1);
		hover.setFromY(1.1);
		hover.setToX(1);
		hover.setToY(1);
		hover.setDuration(Duration.millis(100));
		hover.setNode((Node) event.getSource());
		hover.play();
	}

	@FXML
	private void selectLevel(MouseEvent event) {
		// Only do if screen is active
		if (!isScreenActive) return;

		// levelNumber is which level is being contained
		// Level 1 will have the levelNumber = 0, etc.
		Node level = (Node) event.getSource();	
		int levelNumber = levelContainer.getChildren().indexOf(level);
		
		// Set the effect on the level icon
		DropShadow selectedLevelEffect = new DropShadow();
		selectedLevelEffect.setHeight(20d);
		selectedLevelEffect.setWidth(20d);
		selectedLevelEffect.setSpread(0.3d);
		selectedLevelEffect.setColor(Color.WHITE);
		level.setEffect(selectedLevelEffect);
		
		// Clear the effects of the old selected level
		if (selectedLevel != -1) {	
			Node oldSelectedLevel = levelContainer.getChildren().get(selectedLevel);
			oldSelectedLevel.setEffect(null);
		}

		// Changing the selected level
		
		if (selectedLevel != levelNumber) {
			selectedLevel = levelNumber;
			enableBattle();
		} else {
			selectedLevel = -1;
			disableBattle();
		}

	}

	private void disableBattle() {
		battleIcon.setImage(greyScaleBattleIcon);
		battleIcon.setOnMouseEntered(null);
		battleIcon.setOnMouseExited(null);
		battleIcon.setOnMouseClicked(null);
	}

	private void enableBattle() {
		battleIcon.setImage(originalBattleIcon);
		battleIcon.setOnMouseEntered(e -> {buttonHoverEnter(e);});
		battleIcon.setOnMouseExited(e -> {buttonHoverExit(e);});
		battleIcon.setOnMouseClicked(e -> {battle(e);});
	}

	private void battle(MouseEvent ev) {
		// Only do if screen is active
		if (!isScreenActive) return;

		isScreenActive = false;

		SpaceDeck.transitionToScene(((Node) ev.getSource()).getScene(), SpaceDeck.SceneType.BattleScreen);
	}

	private Image makeGreyScale(ImageView img) {
		Image image = img.getImage();
		
		PixelReader px = image.getPixelReader();
		WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
		PixelWriter pwNewImage = newImage.getPixelWriter();

		for (int i = 0; i < (int) image.getWidth(); i++) {
			for (int j = 0; j < (int) image.getHeight(); j++) {
				Color color = px.getColor(i, j);
				// Magic numbers make the values grey scale relative to human sight
				// I looked this up on google dont judge
				// Also i was too lazy to make the grey scale images properly and this was more fun
				double greyColor = color.getRed() * 0.3 + color.getBlue() * 0.59 + color.getGreen() * 0.11;
				pwNewImage.setColor(i, j, new Color(greyColor, greyColor, greyColor, color.getOpacity()));
			}
		}
		
		return newImage;
	}

	public void setPlanet(Planet p) {
		planetImage.setImage(new Image(SpaceDeck.class.getResourceAsStream("/spacedeck/media/" + p.getName() + ".png")));
		planetName.setText(p.getName());
		description.setText(p.getDescription());
		population.setText(Integer.toString(p.getPopulation()));
		diameter.setText(p.getDiameter() + " KM");
		environment.setText(p.getEnvironment());
		
		ArrayList<Opponent> champList = p.getChampions();

		for (Opponent o : champList) {
			Text champ = new Text(o.getName());
			champ.setFont(Font.font("System", FontWeight.BOLD, 14d));
			champ.setFill(Color.WHITE);
			championsList.getChildren().add(champ);
		}		
	}

	@FXML
	private void returnToMap(MouseEvent event) {
		// Only do if screen is active
		if (!isScreenActive) return;
		
		isScreenActive = false;
		SpaceDeck.transitionToScene(((Node) event.getSource()).getScene(), SpaceDeck.SceneType.MapScreen);
	}
}
