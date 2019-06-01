package xt449.minecraftgamelibrary.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryInterfaceBuilder {
	private final InventoryInterfaceItem[] items;
	private final String title;
	private final Player player;
	private final boolean autoUpdate;
	private final InventoryInterfaceHolder holder = new InventoryInterfaceHolder();

	public InventoryInterfaceBuilder(int rows, String title, Player player, boolean autoUpdate) {
		this.items = new InventoryInterfaceItem[rows * 9];
		this.title = title;
		this.player = player;
		this.autoUpdate = autoUpdate;
	}

	public InventoryInterfaceBuilder(int rows, String title, boolean autoUpdate) {
		this.items = new InventoryInterfaceItem[rows * 9];
		this.title = title;
		this.player = null;
		this.autoUpdate = autoUpdate;
	}

	public InventoryInterfaceBuilder(int rows, String title, Player player) {
		this.items = new InventoryInterfaceItem[rows * 9];
		this.title = title;
		this.player = player;
		this.autoUpdate = true;
	}

	public InventoryInterfaceBuilder(int rows, String title) {
		this.items = new InventoryInterfaceItem[rows * 9];
		this.title = title;
		this.player = null;
		this.autoUpdate = true;
	}

	public final InventoryInterfaceBuilder addItem(InventoryInterfaceItem item, int slot) {
		this.items[slot] = item;
		return this;
	}

	public final InventoryInterfaceBuilder addItems(InventoryInterfaceItem... itemArray) {
		System.arraycopy(itemArray, 0, this.items, 0, itemArray.length);
		return this;
	}

	public InventoryInterface build() {
		Inventory inventory = Bukkit.createInventory(this.holder, this.items.length, this.title);

		for(int i = 0; i < this.items.length; ++i) {
			if (this.items[i] != null) {
				inventory.setItem(i, this.items[i].itemStack);
			}
		}

		this.holder.inventory = inventory;
		return InventoryInterfaceManager.register(this.holder.uuid, new InventoryInterface(inventory, this.items, this.player, this.autoUpdate));
	}
}
