/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package spacedeck;

import java.io.FileNotFoundException;
import spacedeck.model.WaterCard;
import spacedeck.model.Planet;
import spacedeck.model.EarthCard;
import spacedeck.model.Card;
import spacedeck.model.Opponent;
import spacedeck.model.AILevel;
import spacedeck.model.FireCard;
import spacedeck.model.AirCard;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.json.simple.parser.ParseException;
import spacedeck.model.Player;

/**
 *
 * @author pj
 */
public class SpaceDeck extends Application {
	private static Player currentPlayer;
	private static Stage currentStage;

	public static final int[] DEFAULT_RESOLUTION = { 960 , 540 };

	public enum SceneType {
		TitleScreen, CollectionScreen, MapScreen, LevelSelectionScreen, BattleScreen, SettingsScreen
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
				newPlanet.getChampions().add(new Opponent((String) s, 20, 1, AILevel.ADVANCED));
			}
		}
		
		JSONObject settings = (JSONObject) fastOpenJSON("src/spacedeck/Settings.json");
	
		currentStage = new Stage();
        currentStage.setHeight(((double) settings.get("scale")) * DEFAULT_RESOLUTION[1] + 30);
        currentStage.setWidth(((double) settings.get("scale")) * DEFAULT_RESOLUTION[0] + 10);
		
		/*
			-------------
			SCENE LOADING
			-------------
		*/
		FXMLLoader sceneLoader = loadScene(SceneType.BattleScreen);

		currentStage.setScene(((Parent) sceneLoader.getRoot()).getScene());
		currentStage.setResizable(false);
        currentStage.show();
		currentStage.setMinHeight(0);
		currentStage.setMinWidth(0);
		currentStage.setMaxHeight(Integer.MAX_VALUE);
		currentStage.setMaxWidth(Integer.MAX_VALUE);
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
			case SettingsScreen:
				sceneLoader = new FXMLLoader(SpaceDeck.class.getResource("settingsscreen/SettingsScreen.fxml"));
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

		JSONObject settings = (JSONObject) fastOpenJSON("src/spacedeck/Settings.json");
		double scale = (double) settings.get("scale");

		newScene.getRoot().setScaleX(scale);
		newScene.getRoot().setScaleY(scale);
		newScene.getRoot().setLayoutX(DEFAULT_RESOLUTION[0] * (scale - 1) / 2 + 120 * Math.abs(scale - 1)); //magic numbers, sorry
		newScene.getRoot().setLayoutY(DEFAULT_RESOLUTION[1] * (scale - 1) / 2 + 40 * Math.abs(scale - 1)); //thru trial & error not math
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

	public static void changeSetting(String setting, double value) {
		JSONObject settings = (JSONObject) fastOpenJSON("src/spacedeck/Settings.json");
		
		if (settings.containsKey(setting)) {
			settings.put(setting, value);
		} else {
			System.out.println("[ERROR] Tried to modified a non-existent setting.");
		}

		PrintWriter writer;

		try {
			writer = new PrintWriter("src/spacedeck/Settings.json");
		} catch (IOException e) {
			System.out.println("[ERROR] IOException loading Settings file.");
			return;
		} 

		writer.print(settings.toJSONString());
		writer.flush();
		writer.close();
	}

	public static Object getSetting(String setting) {
		JSONObject settings = (JSONObject) fastOpenJSON("src/spacedeck/Settings.json");

		return settings.get(setting);
	}

	/**
	 * Opens a JSON file, automatically managing possible errors. <br>
	 * Dependencies: {@code org.json.simple.*} <br>
	 * Errors are managed automatically by printing the respective error and the path of the file where the error occured.
	 * @param path The path to the JSON file. In the format: <br>
	 * <p style="color: green"> {@code "src/[root folder]/[folder]/[file].json"} </p>
	 * @return Object that represents the root structure of the JSON file.
	 * <br>
	 * Structures include: {@code JSONObject}, {@code JSONArray}, and other data
	 * structures provided in the {@code org.json.simple} library.
	 * 
	 */
	public static Object fastOpenJSON(String path) {
		JSONParser parser = new JSONParser();
		JSONObject object;
		try {
			object = (JSONObject) parser.parse(new FileReader("src/spacedeck/Settings.json"));
			return object;
		} catch (IOException e) {
			System.out.println("[ERROR] IOException loading file. " + path);
		} catch (ParseException e) {
			System.out.println("[ERROR] JSON file is corrupt and could not be parsed. " + path);
		}

		return null;
	}
}
