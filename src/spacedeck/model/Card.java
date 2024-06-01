package spacedeck.model;
import java.util.ArrayList;
import java.util.Random;
import spacedeck.exceptions.GearAlreadyUsedException;
import spacedeck.exceptions.GearLimitExceededException;

public abstract class Card implements Attackable, Deckable, Cloneable {
    // Constants
    private static final int MAXGEARS = 1;
    
    // Card Properties
    private String name, region, element, description, icon;
    private String strongAgainst, weakAgainst;
    private ArrayList<Gear> currentGears = new ArrayList<>();
	private static ArrayList<Card> allCards = new ArrayList<>();
    private int cost, attack, health, maxHealth;
    private int level = 0, xp = 0;
    private boolean isActive = false;
	private boolean isLegendary;
    
    /**
     * @param n Name of the card.
     * @param r Region of the card.
     * @param e Element of the card.
     * @param s Element the card is strong against.
     * @param w Element the card is weak against.
     * @param c Fuel cost it takes to deploy the card.
     * @param a Attack of the card.
     * @param h Health of the card.
     */
    public Card(String n, String r, String e, String s, String w, int c, int a, int h) {
        this.name = n;
        this.region = r;
        this.element = e;
        this.strongAgainst = s;
        this.weakAgainst = w;
        this.description = "???";
        this.cost = c;
        this.attack = a;
        this.maxHealth = h;
        this.health = h;
        this.icon = "";
		this.isLegendary = false;
		
		allCards.add(this);
    }

    /**
     * @param n Name of the card.
     * @param r Region of the card.
     * @param e Element of the card.
     * @param s Element the card is strong against.
     * @param w Element the card is weak against.
     * @param c Fuel cost it takes to deploy the card.
     * @param a Attack of the card.
     * @param h Health of the card.
	 * @param l Whether or not the card is legendary (icon will be full screen)
     */
    public Card(String n, String r, String e, String s, String w, int c, int a, int h, boolean l) {
        this.name = n;
        this.region = r;
        this.element = e;
        this.strongAgainst = s;
        this.weakAgainst = w;
        this.description = "???";
        this.cost = c;
        this.attack = a;
        this.maxHealth = h;
        this.health = h;
        this.icon = "";
		this.isLegendary = l;
		
		allCards.add(this);
    }

	/**
	 * Creates a clone of a card from an existing one.
	 * 
	 * @param card 
	 * The card to be cloned and whose information will be copied. 
	 * 
	 * @return
	 * A copy of the inputted Card.
	 */
	public static Card cloneCard(Card card) {
		Card newCard = null;
		
		if (card.getElement().equals("Water")) {
			newCard = new WaterCard(card.name, card.region, card.cost, card.attack, card.health, card.isLegendary);
		} else if (card.getElement().equals("Fire")) {
			newCard = new FireCard(card.name, card.region, card.cost, card.attack, card.health, card.isLegendary);
		} else if (card.getElement().equals("Earth")) {
			newCard = new EarthCard(card.name, card.region, card.cost, card.attack, card.health, card.isLegendary);
		} else if (card.getElement().equals("Air")) {
			newCard = new AirCard(card.name, card.region, card.cost, card.attack, card.health, card.isLegendary);
		}

		newCard.currentGears.addAll(card.currentGears);
		newCard.description = card.description;
		newCard.icon = card.icon;

		// There is no need for the card to be added to the allCards list, so remove it
		allCards.remove(newCard);

		return newCard;
	}
    
    /**
     * Adds a gear to the card.
     * @param g The gear to be added to the Card.
     * @throws GearLimitExceededException if more than 1 gear is added.
     * @throws GearAlreadyUsedException if the gear is already in use by another card.
     */
    public void equipGear(Gear g) throws GearLimitExceededException, GearAlreadyUsedException {
        if (currentGears.size() >= MAXGEARS) {
            throw new GearLimitExceededException("You cannot add more than " + MAXGEARS + " gears.");
        }
        
        if (!g.getAvailability()) {
            throw new GearAlreadyUsedException("Gear \"" + g.getName() + "\" is already in use.");
        }
        
        // If the gear can be added, 
        // add it to currentGears and add attack and health boost
        currentGears.add(g);
        attack += g.getAttackFx();
        health += g.getHealthFx();
        g.setAvailability(false);
    }
    
    @Override
    public void attack(Card c) {
        // Just in case we need to scale down the damage
        int dmg = attack;
        
        if (this.strongAgainst.equals(c.getElement())) {
            dmg *= 2;
        }
        
        c.takeDamage(dmg);
    }
    
    @Override
    public void attack(Character ch) {
        // Just in case we need to scale down the damage
        int dmg = attack;
        
        ch.takeDamage(dmg);
    }
    
    @Override
    public void takeDamage(int dmg) {
        health -= dmg;
        
        if (health <= 0) {
			health = 0;
        }
    }
    
    public abstract void specialAttack();
    
    // GETTERS & SETTERS
	public static Card getRandomCard() {
		Random rand = new Random();

		return Card.cloneCard(allCards.get(rand.nextInt(allCards.size())));
	}

	public static Card searchCard(String name) {
		for (Card card : allCards) {
			if (card.getName().equals(name)) {
				return Card.cloneCard(card);
			}
		}

		return null;
	}

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String d) {
        this.description = d;
    }

    public String getRegion() {
        return region;
    }

    public String getElement() {
        return element;
    }
    
    @Override
    public int getCost() {
        return cost;
    }

    public int getHealth() {
        return health;
    }
    
    public void reset() {
        health = maxHealth;
        isActive = false;
    }
    
    public int getAttack() {
        return attack;
    }
    
    public boolean getActivity() {
        return isActive;
	}
    
    public void setActivity(boolean newActivity) {
        isActive = newActivity;
    }
    
    @Override
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String path) {
        icon = path;
    }

	public ArrayList<Gear> getGears() {
		return currentGears;
	}

	public boolean getIsLegendary() {
		return isLegendary;
	}
}
