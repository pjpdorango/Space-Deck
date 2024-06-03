package spacedeck.model;

/**
 * Subclass of Card. Automatically sets the {@code element} to Air, the {@code strongAgainst} to Water, and the {@code weakAgainst} to Earth. 
 * 
 * @author pj
 */
public class AirCard extends Card {
	/**
	 * @param n Name of the card
	 * @param r Region of card's origin
	 * @param c Cost of the card (fuel consumption on deployment)
	 * @param a Attack of the card
	 * @param h Health of the card
	 */
    public AirCard(String n, String r, int c, int a, int h) {
        super(n, r, "Air", "Water", "Earth", c, a, h);
    }
    
	/**
	 * @param n Name of the card
	 * @param c Cost of the card (fuel consumption on deployment)
	 * @param a Attack of the card
	 * @param h Health of the card
	 */
    public AirCard(String n, int c, int a, int h) {
        super(n, "???", "Air", "Water", "Earth", c, a, h);
    }

	/**
	 * @param n Name of the card
	 * @param r Region of card's origin
	 * @param c Cost of the card (fuel consumption on deployment)
	 * @param a Attack of the card
	 * @param h Health of the card
	 * @param l Legendary status of a card. A legendary card will have an icon that spans across the entire card's length.
	 */
    public AirCard(String n, String r, int c, int a, int h, boolean l) {
        super(n, r, "Fire", "Earth", "Water", c, a, h, l);
    }
}
