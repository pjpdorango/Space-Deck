package spacedeck.model;

/**
 * Subclass of Card. Automatically sets the {@code element} to Water, the {@code strongAgainst} to Fire, and the {@code weakAgainst} to Air. 
 * 
 * @author pj
 */
public class WaterCard extends Card {
	/**
	 * @param n Name of the card
	 * @param r Region of card's origin
	 * @param c Cost of the card (fuel consumption on deployment)
	 * @param a Attack of the card
	 * @param h Health of the card
	 */
    public WaterCard(String n, String r, int c, int a, int h) {
        super(n, r, "Water", "Fire", "Air", c, a, h);
    }
    
	/**
	 * @param n Name of the card
	 * @param c Cost of the card (fuel consumption on deployment)
	 * @param a Attack of the card
	 * @param h Health of the card
	 */
    public WaterCard(String n, int c, int a, int h) {
        super(n, "???", "Water", "Fire", "Air", c, a, h);
    }

	/**
	 * @param n Name of the card
	 * @param r Region of card's origin
	 * @param c Cost of the card (fuel consumption on deployment)
	 * @param a Attack of the card
	 * @param h Health of the card
	 * @param l Legendary status of a card. A legendary card will have an icon that spans across the entire card's length.
	 */
    public WaterCard(String n, String r, int c, int a, int h, boolean l) {
        super(n, r, "Fire", "Earth", "Water", c, a, h, l);
    }
}
