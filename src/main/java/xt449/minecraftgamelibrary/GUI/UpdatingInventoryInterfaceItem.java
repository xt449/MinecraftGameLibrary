package xt449.minecraftgamelibrary.GUI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class UpdatingInventoryInterfaceItem extends InventoryInterfaceItem {
	private final UpdatingInventoryInterfaceItem.Updater updater;

	public UpdatingInventoryInterfaceItem(ItemStack itemStack, Action action, UpdatingInventoryInterfaceItem.Updater updater) {
		super(itemStack, action);
		this.updater = updater;
	}

	final ItemStack triggerUpdate(@Nullable Player player, ItemStack itemStack) {
		return this.updater.update(player, itemStack);
	}

	public interface Updater {
		ItemStack update(Player var1, ItemStack var2);
	}
}
