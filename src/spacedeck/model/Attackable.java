package spacedeck.model;

/**
 * Attackable interface implemeted on classes with the ability to attack AND take damage.
 * 
 * @author pj
 */
public interface Attackable {
    void attack(Card c);
    void attack(Character ch);
    void takeDamage(int dmg);
}
