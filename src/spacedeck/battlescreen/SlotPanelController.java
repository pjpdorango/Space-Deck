/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package spacedeck.battlescreen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import spacedeck.model.Card;

/**
 * FXML Controller class
 *
 * @author pj
 */
public class SlotPanelController implements Initializable {

    @FXML
    private Text cardAttack;
    @FXML
    private Text cardHealth;
    @FXML
    private ImageView cardIcon;

	private Card card;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { }
    
	public void setCard(Card c) {
		card = c;

        cardHealth.setText(Integer.toString(c.getHealth()));
		Image icon = new Image(getClass().getResourceAsStream("/spacedeck/media/" + c.getIcon()));
        cardIcon.setImage(icon);
        cardAttack.setText(Integer.toString(c.getAttack()));
	}

	public void updateData() {
        cardHealth.setText(Integer.toString(card.getHealth()));
        cardAttack.setText(Integer.toString(card.getAttack()));
	}

	public Card getCard() {
		return this.card;
	}
}
