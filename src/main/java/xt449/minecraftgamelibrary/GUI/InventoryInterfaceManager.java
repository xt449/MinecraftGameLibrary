package xt449.minecraftgamelibrary.GUI;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryInterfaceManager {

	private static Map<UUID, InventoryInterface> inventoryInterfaces = new HashMap<>();
	private static boolean initialized = false;

	public static void initialize(Plugin plugin) {
		if(!initialized) {
			Bukkit.getPluginManager().registerEvents(new Listener() {
				@EventHandler(priority = EventPriority.HIGHEST)
				public final void onInventoryClick(InventoryClickEvent event) {
					if(event.getView().getTopInventory().getHolder() instanceof InventoryInterfaceHolder) {
						event.setCancelled(true);
						event.setResult(Event.Result.DENY);
						if(event.getClickedInventory() == event.getView().getTopInventory()) {
							InventoryInterfaceManager.getInventoryInterface((InventoryInterfaceHolder)event.getClickedInventory().getHolder()).onClick(event);
						}
					}

				}

				@EventHandler(priority = EventPriority.HIGHEST)
				public final void onInventoryDrag(InventoryDragEvent event) {
					if(event.getView().getTopInventory().getHolder() instanceof InventoryInterfaceHolder) {
						event.setCancelled(true);
						event.setResult(Event.Result.DENY);
					}
				}
			}, plugin);

			initialized = true;
		}
	}

	static InventoryInterface register(UUID uuid, InventoryInterface inventoryInterface) {
		if(!initialized) {
			throw new IllegalMonitorStateException("InventoryInterfaceManager was not initialized by any plugin!");
		} else {
			return inventoryInterfaces.put(uuid, inventoryInterface);
		}
	}

	private static InventoryInterface getInventoryInterface(InventoryInterfaceHolder holder) {
		return inventoryInterfaces.get(holder.uuid);
	}
}
