package spacedeck.model;

public class Gear implements Deckable {
    private String name, icon, description;
    private int cost;
    private int attackFx, healthFx;
    private boolean isAvailable = true;
    
    /**
     * @param n Name of the gear.
     * @param c Fuel cost it takes to deploy the gear.
     * @param a attackFx, or the amount of attack the gear adds to the card.
     * @param h healthFx, or the amount of health the gear adds to the card.
     */
    public Gear(String n, int c, int a, int h) {
        this.name = n;
        this.cost = c;
        this.attackFx = a;
        this.healthFx = h;
    }
    
    // GETTERS & SETTERS
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCost() {
        return cost;
    }
    
    public void setIcon(String path) {
        icon = path;
    }
    
    @Override
    public String getIcon() {
        return icon;
    }
    
    @Override 
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String d) {
        description = d;
    }

    public int getAttackFx() {
        return attackFx;
    }

    public int getHealthFx() {
        return healthFx;
    }
    
    public boolean getAvailability() {
        return isAvailable;
    }
    
    public void setAvailability(boolean newAvailability) {
        isAvailable = newAvailability;
    }
}
