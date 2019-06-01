package xt449.minecraftgamelibrary.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryInterfaceItem {

	final ItemStack itemStack;
	private final Action action;

	public InventoryInterfaceItem(ItemStack itemStack, Action action) {
		this.itemStack = itemStack;
		this.action = action;
	}

	final void triggerAction(InventoryClickEvent event) {
		this.action.onClick(event.getClickedInventory(), (Player)event.getWhoClicked(), event.getSlot(), event.getClick(), event.getHotbarButton());
	}

	public interface Action {
		/**
		 * @param inventory The associated Inventory
		 * @param player The associated Player
		 * @param slot The associated slot value
		 * @param click The associated ClickType
		 * @param hotbarButton The associated hot-bar button value
		 */
		void onClick(Inventory inventory, Player player, int slot, ClickType click, int hotbarButton);
	}
}
