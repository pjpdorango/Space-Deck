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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { }
    
    public void setCardAttack(int a) {
        cardAttack.setText(Integer.toString(a));
    }
    
    public void setCardHealth(int h) {
        cardHealth.setText(Integer.toString(h));
    }
    
    public void setCardIcon(Image i) {
        cardIcon.setImage(i);
    }
    
}
