package spacedeck.model;

public interface Attackable {
    void attack(Card c);
    void attack(Character ch);
    void takeDamage(int dmg);
}
