package spacedeck.model;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.input.KeyCode;
import spacedeck.exceptions.GearAlreadyUsedException;
import spacedeck.exceptions.GearLimitExceededException;

/**
 * Class represents a Card; a unit that can be placed onto slots, attack directly, and attack themselves.
 * @author pj
 */
public abstract class Card implements Attackable, Deckable, Cloneable {
    // Constants
	/**
	 * Maximum number of gears a card can have. <br> <br>
	 * {@code DEFAULT} = 1
	 */
    private static final int MAXGEARS = 1;
    
    // Card Properties
	/**
	 * The card's name. 
	 */
    private String name;
	/**
	 * The card's region of origin. 
	 */
	private String region;
	/**
	 * The card's element. The type circle in the game is as follows: <br>
	 * <h2>The Type Circle</h2>
	 * 
	 * <b>Earth</b> is <span style="color: red;">strong against</span> <b>Air</b>.<br>
	 * <b>Air</b> is <span style="color: red;">strong against</span> <b>Water</b>.<br>
	 * <b>Water</b> is <span style="color: red;">strong against</span> <b>Fire</b>.<br>
	 * <b>Fire</b> is <span style="color: red;">strong against</span> <b>Earth</b>.<br><br>
	 * 
	 * Conversely, the inverse match-ups will be weak against each other.<br><br>
	 * 
	 * <b>Air</b> is <span style="color: blue;">weak against</span> <b>Earth</b>.<br>
	 * <b>Earth</b> is <span style="color: blue;">weak against</span> <b>Fire</b>.<br>
	 * <b>Fire</b> is <span style="color: blue;">weak against</span> <b>Water</b>.<br>
	 * <b>Water</b> is <span style="color: blue;">weak against</span> <b>Air</b>.<br>
	 */
	private String element;
	/**
	 * The element the card is strong against. 
	 * If a card of this element is attacked by this card, the attack will do {@code 2x} the amount of normal damage.
	 * Conversely, if a card of this element attacks this card, the attack will do {@code 1/2x} the amount of normal damage. <br>
	 * For the full type circle, see {@link element}
	 */
    private String strongAgainst;
	/**
	 * The element the card is weak against. 
	 * If a card of this element is attacked by this card, the attack will do {@code 1/2x} the amount of normal damage.
	 * Conversely, if a card of this element attacks this card, the attack will do {@code 2x} the amount of normal damage.
	 * For the full type circle, see {@link element}
	 */
	private String weakAgainst;
	/**
	 * The filename of the card's icon.
	 * The specified file name under this String will automatically be prefixed with this path:
	 * <div style="color: green; padding-left: 10px"> "spacedeck/media/cards/" </div>
	 * 
	 * <br>
	 * <b><i> For example, </b></i> with an {@code icon} = <span style="color: red">"special/Miku.PNG"</span>, the String will refer to the path:
	 * <div style="color: green; padding-left: 10px"> "spacedeck/media/cards/special/Miku.PNG" </div>
	 */
	private String icon;
	/**
	 * The card's description.
	 */
	private String description;
	/**
	 * The gears currently and actively attached to the card. <br>
	 * It should always be the case that {@code currentGears.size() <= MAXGEARS}.
	 */
    private ArrayList<Gear> currentGears = new ArrayList<>();
	/**
	 * A list that compiles every card to have ever been constructed. Contains no duplicates, or cards with the same name.
	 */
	private static ArrayList<Card> allCards = new ArrayList<>();
	/**
	 * A list that contains every card that can be drawn from the stack of cards. Contains no duplicates, or cards with the same name.
	 */
	private static ArrayList<Card> stackPool = new ArrayList<>();
	/**
	 * The fuel cost of the card.
	 */
    private int cost;
	/**
	 * The attack of the card.
	 */
	private int attack;
	/**
	 * The current health of the card. Must always be {@code <= maxHealth}
	 */
	private int health;
	/**
	 * The maximum health this card may have. 
	 */
	private int maxHealth;
	/**
	 * Boolean that describes whether a card is legendary status or not. Legendary cards have their icons cover the entire background of the card.
	 */
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
    
	/**
	 * Attacks a card.
	 * If the target card is the element this card is strong against, the damage is doubled.
	 * If the target card is the element this card is weak against, the damage is halved.
	 * 
	 * @param c The card to be attacked.
	 */
    @Override
    public void attack(Card c) {
        // Just in case we need to scale down the damage
        int dmg = attack;
        
        if (this.strongAgainst.equals(c.getElement())) {
            dmg *= 2;
        } else if (this.weakAgainst.equals(c.getElement())) {
			dmg /= 2;
		}
        
        c.takeDamage(dmg);
    }
    
	/**
	 * Attacks a character.
	 * 
	 * @param ch The card to be attacked.
	 */
    @Override
    public void attack(Character ch) {
        // Just in case we need to scale down the damage
        int dmg = attack;
        
        ch.takeDamage(dmg);
    }
    
	/**
	 * Takes {@code dmg} amount of damage. Should only be used for an attack or as a means of modifying health.
	 * 
	 * @param dmg The amount of damage to be taken.
	 */
    @Override
    public void takeDamage(int dmg) {
        health -= dmg;
        
        if (health <= 0) {
			health = 0;
        }
    }
    
	/**
	 * Resets all the properties of the card which varies from battle to battle.
	 */
    public void reset() {
        health = maxHealth;
    }
    
	/**
	 * Gets a random card from the list of all cards ever generated.
	 * 
	 * @return A random Card.
	 */
	public static Card getRandomCard() {
		Random rand = new Random();

		return Card.cloneCard(allCards.get(rand.nextInt(allCards.size())));
	}

	/**
	 * Gets a random card from the list of cards that can be taken from the stack. Should only be used for when a character takes a stack from the card.
	 * 
	 * @return A random Card from the card stack pool.
	 */
	public static Card getRandomStackCard() {
		Random rand = new Random();

		return Card.cloneCard(stackPool.get(rand.nextInt(stackPool.size())));
	}

	/**
	 * Searches for a card from the list of all cards ever generated with the given name and returns a <b>copy</b> of the card.
	 * If the target Card is not found, returns {@code null}.
	 * 
	 * @param name The name of the card to be searched.
	 * @return The target Card, or {@code null} if the target is not found.
	 */
	public static Card searchCard(String name) {
		for (Card card : allCards) {
			if (card.getName().equals(name)) {
				return Card.cloneCard(card);
			}
		}

		return null;
	}

    // GETTERS & SETTERS
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
    
    public int getAttack() {
        return attack;
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

	public static ArrayList<Card> getStackPool() {
		return stackPool;
	}

	public boolean getIsLegendary() {
		return isLegendary;
	}
}
