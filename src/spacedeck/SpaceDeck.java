/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package spacedeck;

import spacedeck.model.WaterCard;
import spacedeck.model.Planet;
import spacedeck.model.EarthCard;
import spacedeck.model.Card;
import spacedeck.model.Opponent;
import spacedeck.model.AILevel;
import spacedeck.model.FireCard;
import spacedeck.model.AirCard;
import java.io.FileReader;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import spacedeck.model.Player;

/**
 *
 * @author pj
 */
public class SpaceDeck extends Application {
	private static Player currentPlayer;
	private static Stage currentStage;

	public enum SceneType {
		TitleScreen, CollectionScreen, MapScreen, LevelSelectionScreen, BattleScreen	
	}
	
    @Override
    public void start(Stage stage) throws Exception {        
		/*
			---------------
			!! IMPORTANT !!
			---------------

			The project uses a library for reading the JSON files and we arenot very sure
			how to export the project including the library...

			So for now you have to **add** the library to your classpath

			Add the json.simple-1.1.1.jar file from the project folder as a library by

			- Scrolling down the project
			- Right clicking libraries
			- Add JAR file
			- Select .jar file
			- And voila!!!! Should work now.
		*/		

		// FONT LOADING
        Font.loadFont(getClass().getResource("fonts/Squartiqa4F.ttf").toExternalForm(), 10);
        Font.loadFont(getClass().getResource("fonts/Cristik.ttf").toExternalForm(), 10);
        Font.loadFont(getClass().getResource("fonts/TTSupermolotNeue-Regular.ttf").toExternalForm(), 10);
		
		/*
		-----------------
		JSON FILE READING
		-----------------
		*/	
		// CARD READING
		JSONParser parser = new JSONParser();

		JSONArray array = (JSONArray) parser.parse(new FileReader("src/spacedeck/CardListJSON.json"));
		
		for (Object o : array) {
			JSONObject cardProperties = (JSONObject) o;
			
			String name = (String) cardProperties.get("name");
			String region = (String) cardProperties.get("region");
			String element = (String) cardProperties.get("element");
			int cost = Long.valueOf((long) cardProperties.get("cost")).intValue();
			int attack = Long.valueOf((long) cardProperties.get("attack")).intValue();
			int health = Long.valueOf((long) cardProperties.get("health")).intValue();
			String description = (String) cardProperties.get("description");
			String icon = (String) cardProperties.get("icon");
			
			Card card = null;
			
			if (element.equals("Fire")) {
				card = new FireCard(name, region, cost, attack, health);
			} else if (element.equals("Water")) {
				card = new WaterCard(name, region, cost, attack, health);
			} else if (element.equals("Earth")) {
				card = new EarthCard(name, region, cost, attack, health);
			} else if (element.equals("Air")) {
				card = new AirCard(name, region, cost, attack, health);
			} 
			
			if (card != null) {
				card.setIcon(icon);
				card.setDescription(description);
			}	
		}

		// PLANET READING
		array = (JSONArray) parser.parse(new FileReader("src/spacedeck/PlanetsList.json"));
		for (Object o : array) {
			JSONObject planet = (JSONObject) o;

			String name = (String) planet.get("name");
			String description = (String) planet.get("description");
			int population = Long.valueOf((long) planet.get("population")).intValue();
			int diameter = Long.valueOf((long) planet.get("diameter")).intValue();
			String environment = (String) planet.get("environment");
			JSONArray champions = (JSONArray) planet.get("champions");

			Planet newPlanet = new Planet(name);
			newPlanet.setDescription(description);
			newPlanet.setPopulation(population);
			newPlanet.setDiameter(diameter);
			newPlanet.setEnvironment(environment);
			for (Object s : champions) {
				System.out.println((String) s);
				newPlanet.getChampions().add(new Opponent((String) s, 20, 1, AILevel.ADVANCED));
			}
		}
		
		currentStage = new Stage();
        currentStage.setHeight(570);
        currentStage.setWidth(960);
		
		/*
			-------------
			SCENE LOADING
			-------------
			WHATS NEW? 
			- Planet lore is now accurate (except champions)
			- Battle Screen now has "End Turn" button
				- Button has gradient effect that smooths when hovering
				- Gives control to the Opponent AI to choose their move next
			- Opponent now has ANIMATIONS!!
				- For drawing a card
				- Skipping a turn
		*/
		FXMLLoader sceneLoader = loadScene(SceneType.BattleScreen);

		currentStage.setScene(((Parent) sceneLoader.getRoot()).getScene());
        currentStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

	public static FXMLLoader loadScene(SceneType sceneType) {
		FXMLLoader sceneLoader = null;

		if (null != sceneType) switch (sceneType) {
			case TitleScreen:
				sceneLoader = new FXMLLoader(SpaceDeck.class.getResource("titlescreen/TitleScreen.fxml"));
				break;
			case CollectionScreen:
				sceneLoader = new FXMLLoader(SpaceDeck.class.getResource("collectionscreen/CollectionScreen.fxml"));
				break;
			case LevelSelectionScreen:
				sceneLoader = new FXMLLoader(SpaceDeck.class.getResource("levelselectionscreen/LevelSelectionScreen.fxml"));
				break;
			case MapScreen:
				sceneLoader = new FXMLLoader(SpaceDeck.class.getResource("mapscreen/MapScreen.fxml"));
				break;
			case BattleScreen:
				sceneLoader = new FXMLLoader(SpaceDeck.class.getResource("battlescreen/BattleScreen.fxml"));
				break;
			default:
				break;
		}

		Scene newScene = null;
		try {
			newScene = new Scene(sceneLoader.load());
		} catch (IOException e) {
			System.out.println("[ERROR] Error loading scene: " + e.getMessage());
		}
		
		newScene.setFill(Color.BLACK);

		return sceneLoader;
	}

	public static FXMLLoader transitionToScene(Scene currentScene, SceneType sceneType) {
		FXMLLoader sceneLoader = loadScene(sceneType);
		Parent parent = sceneLoader.getRoot();
		parent.setOpacity(0);

		Parent originalParent = currentScene.getRoot();
		
		FadeTransition fadeOut = new FadeTransition(Duration.millis(400));
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setNode(originalParent);
		fadeOut.setOnFinished((ActionEvent event) -> {
			if (parent.getScene() == null) {
				currentStage.setScene(new Scene(parent));
			} else {
				currentStage.setScene(parent.getScene());
			}
			FadeTransition fadeIn = new FadeTransition(Duration.millis(400));
			fadeIn.setFromValue(0);
			fadeIn.setToValue(1);
			fadeIn.setNode(parent);
			fadeIn.play();
		});
		fadeOut.play();

		return sceneLoader;
	}

	public static Stage getStage() {
		return currentStage;
	}
}
