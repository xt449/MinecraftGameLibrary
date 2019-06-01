package xt449.minecraftgamelibrary.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryInterface {
	private final Inventory inventory;
	private final InventoryInterfaceItem[] items;
	private final Player player;
	private final boolean autoUpdate;

	InventoryInterface(Inventory inventory, InventoryInterfaceItem[] items, Player player, boolean autoUpdate) {
		this.inventory = inventory;
		this.items = items;
		this.player = player;
		this.autoUpdate = autoUpdate;
	}

	final void onClick(InventoryClickEvent event) {
		InventoryInterfaceItem item = this.items[event.getSlot()];

		if (item != null) {
			item.triggerAction(event);
		}
	}

	public void displayTo(Player player) {
		if (this.autoUpdate) {
			this.update();
		}

		player.openInventory(this.inventory);
	}

	public void display() {
		if (this.player != null) {
			if (this.autoUpdate) {
				this.update();
			}

			this.player.openInventory(this.inventory);
		}

	}

	public void update() {
		for(int slot = 0; slot < this.items.length; ++slot) {
			if (this.items[slot] instanceof UpdatingInventoryInterfaceItem) {
				this.inventory.setItem(slot, ((UpdatingInventoryInterfaceItem)this.items[slot]).triggerUpdate(this.player, this.inventory.getItem(slot)));
			}
		}
	}
}
