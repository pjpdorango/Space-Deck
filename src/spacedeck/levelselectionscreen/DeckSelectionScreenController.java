/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package spacedeck.levelselectionscreen;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import spacedeck.SpaceDeck;
import spacedeck.battlescreen.BattleScreenController;
import spacedeck.exceptions.FullDeckException;
import spacedeck.model.Card;
import spacedeck.model.Level;
import spacedeck.model.Planet;
import spacedeck.model.Player;

/**
 * FXML Controller class
 *
 * @author pj
 */
public class DeckSelectionScreenController implements Initializable {

	@FXML
	private GridPane collection;
	@FXML
	private HBox deck;


	private int nextAvailable;
	private Planet planet;
	private Level level;

	private ArrayList<VBox> emptyCards = new ArrayList<>();

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		SpaceDeck.getPlayer().getDeck().clear();

		int row = 0;
		int maxRow = collection.getRowConstraints().size();
		int col = 0;
		int maxCol = collection.getColumnConstraints().size();

		deck.getChildren().forEach(n -> {
			emptyCards.add((VBox) n);
		});
		
		for (Card c : SpaceDeck.getPlayer().getCollection()) {
			FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("CollectionCardDisplayPanel.fxml"));
			VBox card;
			try {
				card = (VBox) cardLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			CollectionCardDisplayPanelController cardController = cardLoader.getController();
			cardController.setCard(c);
			cardController.setSceneController(this);
			cardController.setCol(col);
			cardController.setRow(row);

			collection.add(card, col, row);

			col++;
			if (col >= maxCol) {
				row++;
				col = 0;
			}
		}
	}

	public void removeCardFromDeck(Card card, VBox cardElement, int row, int col) {
		nextAvailable--;
		collection.add(cardElement, col, row);

		deck.getChildren().add(nextAvailable, emptyCards.get(nextAvailable));

		SpaceDeck.getPlayer().getDeck().remove(card);
	}

	public void addCardToDeck(Card card, VBox cardElement, int row, int col) {
		if (nextAvailable >= deck.getChildren().size()) return;

		collection.add(new VBox(), col, row);
		deck.getChildren().remove(nextAvailable);
		deck.getChildren().add(nextAvailable, cardElement);

		try {
			SpaceDeck.getPlayer().addCard(card);
		} catch (FullDeckException e) {
			e.printStackTrace();
		}

		nextAvailable++;
	}

	@FXML
	private void startGame(MouseEvent ev) {
		FXMLLoader loader = SpaceDeck.transitionToScene(((Node) ev.getSource()).getScene(), SpaceDeck.SceneType.BattleScreen);
		BattleScreenController controller = loader.getController();
		controller.setLevel(level);
		controller.setPlanet(planet);
	}

	public void setPlanet(Planet planet) {
		this.planet = planet;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
}
