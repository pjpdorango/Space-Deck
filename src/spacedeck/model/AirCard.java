package spacedeck.model;

public class AirCard extends Card {
    public AirCard(String n, String r, int c, int a, int h) {
        super(n, r, "Air", "Water", "Earth", c, a, h);
    }
    
    public AirCard(String n, int c, int a, int h) {
        super(n, "???", "Air", "Water", "Earth", c, a, h);
    }
	
    @Override
    public void specialAttack() {
        // TODO: Add Special Attack
    }
}
