package spacedeck.model;

public class EarthCard extends Card {
    public EarthCard(String n, String r, int c, int a, int h) {
        super(n, r, "Earth", "Air", "Fire", c, a, h);
    }
    
    public EarthCard(String n, int c, int a, int h) {
        super(n, "???", "Earth", "Air", "Fire", c, a, h);
    }
    
    @Override
    public void specialAttack() {
        // TODO: Add Special Attack
    }
}
