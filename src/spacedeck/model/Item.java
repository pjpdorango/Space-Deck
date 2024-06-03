/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

import javafx.scene.image.Image;

/**
 * Helper class to disseminate information on Item rewards per level.
 * 
 * @author pj
 */
public class Item {
	/**
	 * The specified file name under this String will automatically be prefixed with this path:
	 * <div style="color: green; padding-left: 10px"> "spacedeck/media/" </div>
	 * 
	 * <br>
	 * <b><i> For example, </b></i> with an {@code itemIcon} = <span style="color: red">"special/Glove.PNG"</span>, the String will refer to the path:
	 * <div style="color: green; padding-left: 10px"> "spacedeck/media/special/Glove.PNG" </div>
	 */
	private Image itemIcon;
	/**
	 * The name of the item.
	 */
	private String name;
	/**
	 * The amount of the item.
	 */
	private int amount;

	/**
	 * Constructor for the Item class.
	 * Automatically sets amount to 1.
	 * 
	 * @param n Item name.
	 * @param i Item icon.
	 */
	public Item(String n, Image i) {
		itemIcon = i;
		name = n;
		amount = 1;
	}

	/**
	 * Constructor for the Item class.
	 * 
	 * @param n Item name.
	 * @param i Item icon.
	 * @param a Item amount.
	 */
	public Item(String n, Image i, int a) {
		itemIcon = i;
		name = n;
		amount = a;
	}

	// GETTERS & SETTERS
	public Image getItemIcon() {
		return itemIcon;
	}

	public void setItemIcon(Image itemIcon) {
		this.itemIcon = itemIcon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
