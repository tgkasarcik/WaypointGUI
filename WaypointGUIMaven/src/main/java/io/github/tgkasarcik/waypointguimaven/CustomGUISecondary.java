package io.github.tgkasarcik.waypointguimaven;

import org.bukkit.inventory.ItemStack;

/**
 * Interface for CustomGUIs with secondary methods
 * 
 * @author T. Kasarcik
 *
 */
public abstract interface CustomGUISecondary extends CustomGUI {

	/**
	 * Advances the item at {@code index} to the next item.
	 * 
	 * @param index index of the item to advance
	 */
	void advanceItem(int index);

	/**
	 * Sets first (|this| - 1) slots in {@code this} to be {@code defaultItem} and
	 * the last to be {@code exitButton}.
	 * 
	 * @param defaultItem default item for empty GUI slots
	 * @param exitButton  item to be placed in last slot
	 */
	public default void initializeItems(ItemStack defaultItem, ItemStack exitButton) {
		for (int i = 0; i < this.size() - 1; i++) {
			this.setItem(i, defaultItem);
		}
		this.setItem(this.size() - 1, exitButton);
	}

	/**
	 * Returns the index of the first inventory slot in {@code this} that is not
	 * filled with a {@code defaultItem}.
	 * 
	 * @param defaultItem the specified default item.
	 * @return Index of first empty index, -1 if there are no empty indices.
	 */
	public default int firstEmptyIndex(ItemStack defaultItem) {
		int index = -1;
		for (int i = 0; i < this.size() - 1; i++) {
			if (this.item(i).equals(defaultItem)) {
				index = i;
				break;
			}
		}
		return index;
	}

}
