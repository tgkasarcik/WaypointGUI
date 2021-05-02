package io.github.tgkasarcik.waypointguimaven;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Kernel method interface for CustomGUI
 * 
 * @author T. Kasarcik
 *
 */
public interface CustomGUI {

	/**
	 * Set item at {@code index} to {@code item}
	 * 
	 * @param index index to set item of
	 * @param item  item to set index to
	 */
	void setItem(int index, ItemStack item);

	/**
	 * Displays internal inventory representation of {@code this} to player stored
	 * in {@code this}.
	 *
	 */
	void display();

	/**
	 * Returns the size of {@code this}.
	 */
	int size();

	/**
	 * Returns the {@code Inventory} object upon which {@code this} is built.
	 * 
	 * @return
	 */
	Inventory inventory();

	/**
	 * Returns the {@code ItemStack} at the specified index of {@code this}.
	 * 
	 * @param index index to return {@code ItemStack} of.f
	 * @return
	 */
	ItemStack item(int index);

}
