/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

import java.util.ArrayList;

/**
 * Utility class for storing the information of a planet, as well as its levels.
 * @author pj
 */
public class Planet {
	private String name, description;
	private int population, diameterInKm;
	private String environment;
	private ArrayList<Opponent> knownChampions;
	private ArrayList<Level> levels;
	private static ArrayList<Planet> planets = new ArrayList<>(); 

	public Planet(String n) {
		this.name = n;
		this.description = "???";
		this.environment = "???";
		this.population = -1;
		this.population = -1;
		this.knownChampions = new ArrayList<>();
		this.levels = new ArrayList<>();

		planets.add(this);
	}

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

	public static Planet searchPlanet(String s) {
		for (Planet p : planets) {
			if (p.getName().equals(s)) {
				return p;
			}
		}

		return null;
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
