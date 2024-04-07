package spacedeck.model;

public class WaterCard extends Card {
    public WaterCard(String n, String r, int c, int a, int h) {
        super(n, r, "Water", "Fire", "Air", c, a, h);
    }
    
    public WaterCard(String n, int c, int a, int h) {
        super(n, "???", "Water", "Fire", "Air", c, a, h);
    }
    
    @Override
    public void specialAttack() {
        // TODO: Add Special Attack
    }
}
