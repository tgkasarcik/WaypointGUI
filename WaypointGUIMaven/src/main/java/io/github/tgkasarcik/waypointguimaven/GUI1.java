package io.github.tgkasarcik.waypointguimaven;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class GUI1 implements CustomGUISecondary {

	/*
	 * Private Members --------------------------------------------------------
	 */

	/**
	 * Constant to hold default size of inventory object.
	 */
	private static final int DEFAULT_SIZE = 9;

	/**
	 * Inventory object to act as GUI
	 */
	private Inventory inv;

	/**
	 * ItemStack List to hold all items that are allowed to populate inventory slots
	 * in GUI.
	 */
	private static List<Material> allowedItems;

	/**
	 * Player that owns {@code Inventory} used to represent {@code this}.
	 */
	private Player player;
	
	/*
	 * Protected members ------------------------------------------------------
	 */
	
	/**
	 * {@code ItemStack} object to store items to be used for empty GUI slots.
	 */
	protected static ItemStack EMPTY_SLOT_ITEM;

	/**
	 * {@code ItemStack} object to store items to be used for GUI exit button.
	 */
	protected static ItemStack EXIT_BUTTON;

	/*
	 * Constructors -----------------------------------------------------------
	 */

	/**
	 * Initialize Static variables.
	 */
	static {

		/*
		 * Initialize {@code EMPTY_SLOT_ITEM}.
		 */
		GUI1.EMPTY_SLOT_ITEM = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemMeta meta = GUI1.EMPTY_SLOT_ITEM.getItemMeta();
		meta.setDisplayName("- Empty Slot -");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.RESET + "Use " + ChatColor.LIGHT_PURPLE + "/waypoint create <name>" + ChatColor.RESET
				+ " to create");
		meta.setLore(lore);
		GUI1.EMPTY_SLOT_ITEM.setItemMeta(meta);

		/*
		 * Initialize {@code EXIT_BUTTON}
		 */
		GUI1.EXIT_BUTTON = new ItemStack(Material.BARRIER);
		meta = GUI1.EXIT_BUTTON.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "" + ChatColor.BOLD + "Exit");
		GUI1.EXIT_BUTTON.setItemMeta(meta);

		/*
		 * Initialize {@code allowedItems}
		 */
		allowedItems = new ArrayList<Material>();
		// TODO: change this to load from config.yml
		allowedItems.add(Material.GRASS_BLOCK);
		allowedItems.add(Material.STONE);
		allowedItems.add(Material.SAND);
		allowedItems.add(Material.ICE);
		allowedItems.add(Material.TERRACOTTA);
	}

	/**
	 * Default no-arg constructor.
	 */
	public GUI1() {
		this.inv = Bukkit.createInventory(null, DEFAULT_SIZE);
		this.player = null;
		this.initializeItems(EMPTY_SLOT_ITEM, EXIT_BUTTON);
	}

	/**
	 * Constructor with arguments for target player and desired inventory size.
	 * 
	 * @requires invSize is a multiple of 9 between 9 and 54.
	 * @param p       Target player
	 * @param invSize Desired inventory size
	 */
	public GUI1(Player p, int invSize) {
		this.inv = Bukkit.createInventory(p, invSize);
		this.player = p;
		this.initializeItems(EMPTY_SLOT_ITEM, EXIT_BUTTON);
	}

	/**
	 * Constructor with arguments for target player only.
	 * 
	 * @param p
	 */
	public GUI1(Player p) {
		this.inv = Bukkit.createInventory(p, DEFAULT_SIZE, p.getDisplayName() + "'s Waypoints");
		this.player = p;
		this.initializeItems(EMPTY_SLOT_ITEM, EXIT_BUTTON);
	}

	/*
	 * Kernel Methods ---------------------------------------------------------
	 */

	@Override
	public void setItem(int index, ItemStack item) {
		inv.setItem(index, item);
	}

	@Override
	public void display() {
		this.player.openInventory(this.inv);
	}

	@Override
	public void rename(String newName) {
		Inventory newInv = Bukkit.createInventory(this.player, this.inv.getSize(), newName);
		newInv.setContents(this.inv.getContents());
		this.inv.clear();
		this.inv = newInv;
	}

	@Override
	public void advanceItem(int index) {
		ItemStack currItem = this.inv.getItem(index);
		int listIndex = allowedItems.indexOf(currItem.getType());
		listIndex++;
		if (listIndex >= allowedItems.size()) {
			listIndex = 0;
		}
		this.inv.setItem(index,
				WaypointManager.createItem(allowedItems.get(listIndex), currItem.getItemMeta().getDisplayName()));
	}

	@Override
	public int size() {
		return this.inv.getSize();
	}

	@Override
	public int indexOf(String itemName) {
		int index = -1;
		for (int i = 0; i < this.inv.getSize(); i++) {
			ItemStack currItem = this.inv.getItem(i);
			String currName = ChatColor.stripColor(currItem.getItemMeta().getDisplayName());
			if (currName.equalsIgnoreCase(itemName)) {
				index = i;
				break;
			}
		}
		return index;
	}

	@Override
	public Inventory inventory() {
		return this.inv;
	}

	@Override
	public ItemStack item(int index) {
		return this.inv.getItem(index);
	}

	/*
	 * Private helper methods -------------------------------------------------
	 */

}
