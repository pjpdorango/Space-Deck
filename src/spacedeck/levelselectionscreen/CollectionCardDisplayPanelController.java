/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package spacedeck.levelselectionscreen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import spacedeck.SpaceDeck;
import spacedeck.model.Card;

/**
 * FXML Controller class
 *
 * @author pj
 */
public class CollectionCardDisplayPanelController implements Initializable {

	@FXML
	private VBox card;
	@FXML
	private Text cardCost;
	@FXML
	private ImageView cardElement;
	@FXML
	private ImageView cardIcon;
	@FXML
	private Text cardName;
	@FXML
	private Text cardDescription;

	private int col, row;
	private Card thisCard;
	private DeckSelectionScreenController sceneController;
	private boolean isSelected;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		isSelected = false;
	}	

	@FXML
	private void selectCard(MouseEvent event) {
		if (!isSelected) {
			isSelected = true;
			sceneController.addCardToDeck(thisCard, card, row, col);
		} else {
			isSelected = false;
			sceneController.removeCardFromDeck(thisCard, card, row, col);
		}
	}

	public void setCard(Card c) {
        thisCard = c;
		cardCost.setText(Integer.toString(c.getCost()));
		Image icon = new Image(getClass().getResourceAsStream("/spacedeck/media/" + thisCard.getIcon()));
		try {
			if (thisCard instanceof Card && ((Card) thisCard).getIsLegendary()) {
				card.setStyle("-fx-background-image: url(/spacedeck/media/" + thisCard.getIcon() + "); -fx-background-size: 100% 100%");
				cardIcon.setImage(null);
			} else {
				cardIcon.setImage(icon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		cardName.setText(c.getName());
		cardDescription.setText(c.getDescription());

		String elementName = ((Card) thisCard).getElement();
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
		cardElement.setImage(elementIcon);
	}

	public void setSceneController(DeckSelectionScreenController sceneController) {
		this.sceneController = sceneController;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void setRow(int row) {
		this.row = row;
	}
}
