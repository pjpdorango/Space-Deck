package spacedeck.model;

/**
 * Subclass of Card. Automatically sets the {@code element} to Fire, the {@code strongAgainst} to Earth, and the {@code weakAgainst} to Water. 
 * 
 * @author pj
 */
public class FireCard extends Card {
	/**
	 * @param n Name of the card
	 * @param r Region of card's origin
	 * @param c Cost of the card (fuel consumption on deployment)
	 * @param a Attack of the card
	 * @param h Health of the card
	 */
    public FireCard(String n, String r, int c, int a, int h) {
        super(n, r, "Fire", "Earth", "Water", c, a, h);
    }
    
	/**
	 * @param n Name of the card
	 * @param c Cost of the card (fuel consumption on deployment)
	 * @param a Attack of the card
	 * @param h Health of the card
	 */
    public FireCard(String n, int c, int a, int h) {
        super(n, "???", "Fire", "Earth", "Water", c, a, h);
    }

	/**
	 * @param n Name of the card
	 * @param r Region of card's origin
	 * @param c Cost of the card (fuel consumption on deployment)
	 * @param a Attack of the card
	 * @param h Health of the card
	 * @param l Legendary status of a card. A legendary card will have an icon that spans across the entire card's length.
	 */
    public FireCard(String n, String r, int c, int a, int h, boolean l) {
        super(n, r, "Fire", "Earth", "Water", c, a, h, l);
    }
}
