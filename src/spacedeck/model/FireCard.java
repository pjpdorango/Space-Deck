package spacedeck.model;

public class FireCard extends Card {
    public FireCard(String n, String r, int c, int a, int h) {
        super(n, r, "Fire", "Earth", "Water", c, a, h);
    }
    
    public FireCard(String n, int c, int a, int h) {
        super(n, "???", "Fire", "Earth", "Water", c, a, h);
    }

    public FireCard(String n, String r, int c, int a, int h, boolean l) {
        super(n, r, "Fire", "Earth", "Water", c, a, h, l);
    }
    
    @Override
    public void specialAttack() {
        // TODO: Add Special Attack
    }
}
