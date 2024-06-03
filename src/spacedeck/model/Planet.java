/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

import java.util.ArrayList;

/**
 * Utility class for storing the information of a planet, as well as its levels.
 * 
 * @author pj
 */
public class Planet {
	/**
	 * Name of the planet.
	 */
	private String name;
	/**
	 * Description of the planet.
	 */
	private String description;
	/**
	 * Population of the planet.
	 */
	private int population;
	/**
	 * Diameter of the planet in kilometers.
	 */
	private int diameterInKm;
	/**
	 * Description of the planet's environment.
	 */
	private String environment;
	/**
	 * A list of all of the Opponents in this planet.
	 */
	private ArrayList<Opponent> knownChampions;
	/**
	 * A list of all of the Levels in this planet. There will always be 4 levels in a planet.
	 */
	private ArrayList<Level> levels;
	/**
	 * A static list of all of the planets in the game. As of June 2, 2024, the current planets are:
	 * <ol>
	 * <li><b>Cryoterra</b>, the Unforgiving Land of Ice</li>
	 * <li><b>Lunaris IX</b>, the Blooming Flower of Technology</li>
	 * <li><b>Auros Minoris</b>, the Tiny yet Fearsome Land</li>
	 * <li><b>Astraforge</b>, the Barren Hellscape</li>
	 * </ol>
	 * More details can be found at <span style="color: green;">src/spacedeck/PlanetsList.json</span>.
	 */
	private static ArrayList<Planet> planets = new ArrayList<>(); 

	/**
	 * The constructor for the Planet class. <br>
	 * On default, the description and environment are set to ???, and the population is set to {@code -1}.
	 * @param n Name of the planet.
	 */
	public Planet(String n) {
		this.name = n;
		this.description = "???";
		this.environment = "???";
		this.population = -1;
		this.knownChampions = new ArrayList<>();
		this.levels = new ArrayList<>();

		planets.add(this);
	}

	/**
	 * Searches for a planet with the same given name within the list of all planets constructed and returns it. If the planet does not exist, returns {@code null}.
	 * 
	 * @param s Name of the target planet.
	 * @return Desired planet, or {@code null} if planet does not exist.
	 */
	public static Planet searchPlanet(String s) {
		for (Planet p : planets) {
			if (p.getName().equals(s)) {
				return p;
			}
		}

		return null;
	}

	/**
	 * Adds a level to the Planet's list of levels.
	 * Also assigns the level's planet to be this planet.
	 * 
	 * @param l The level to be added.
	 */
	public void addLevel(Level l) {
		levels.add(l);
		l.setPlanet(this);
	}

	// GETTERS & SETTERS
	public void setDescription(String d) {
		this.description = d;
	}

	public void setPopulation(int p) {
		this.population = p;
	}

	public void setDiameter(int d) {
		this.diameterInKm = d;
	}

	public void setEnvironment(String e) {
		this.environment = e;
	}

	public ArrayList<Opponent> getChampions() {
		return this.knownChampions;
	}

	public ArrayList<Level> getLevels() {
		return this.levels;
	}

	public static ArrayList<Planet> getPlanets() {
		return planets;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getPopulation() {
		return population;
	}

	public int getDiameter() {
		return diameterInKm;
	}

	public String getEnvironment() {
		return environment;
	}
}
