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
	 * Renames {@code this} to the name specified by {@code newName}.
	 * 
	 * @param newName specified new name.
	 */
	void rename(String newName);
	
	/**
	 * Advances the item at {@code index} to the next item.
	 * 
	 * @param index index of the item to advance
	 */
	void advanceItem(int index);

	/**
	 * Returns the size of {@code this}.
	 */
	int size();

	/**
	 * Returns the index of the {@code ItemStack} with the name {@code itemName} in
	 * {@code this}, or -1 if it does not exist.
	 * 
	 * @param itemName Name of item to get index of
	 * @return index of specified item or -1
	 */
	int indexOf(String itemName);

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
