/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spacedeck.model;

import javafx.scene.image.Image;

/**
 *
 * @author pj
 */
public class Item {
	private Image itemIcon;
	private String name;
	private int amount;

	public Item(String n, Image i) {
		itemIcon = i;
		name = n;
		amount = 1;
	}

	public Item(String n, Image i, int a) {
		itemIcon = i;
		name = n;
		amount = a;
	}

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
