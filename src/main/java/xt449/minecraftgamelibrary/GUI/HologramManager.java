package xt449.minecraftgamelibrary.GUI;

import org.bukkit.plugin.Plugin;

import java.util.*;

public class HologramManager {

	private static List<Hologram> holograms = new ArrayList<>();
	private static boolean initialized = false;

	public static void initialize(Plugin plugin) {
		if(!initialized) {
			/*ProtocolManager protocol = ProtocolLibrary.getProtocolManager();

			protocol.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.SPAWN_ENTITY) {
				@Override
				public final void onPacketSending(PacketEvent event) {
					*//*System.out.println(":: " + event.getPacketType() + " ::");*//*

					if(event.getPacket().getIntegers().read(0) == EntityObjectTypes.ArmorStand) {
						final ChunkPosition position = new ChunkPosition((int) (event.getPacket().getDoubles().read(0) / 16), (int) (event.getPacket().getDoubles().read(1) / 16)).normalized();
						if(MinecraftCivilization.getPlayerNation(event.getPlayer()).hasVisionAt(position)) {
							event.setCancelled(true);
						}
					}

					*//*for(Object object : event.getPacket().getModifier().getValues()) {
						if(object != null) {
							if(object instanceof Collection) {
								System.out.println("> Array List {");
								//Collection list = ;
								for(Object element : (Collection) object) {
									if(element instanceof DataWatcher.Item) {
										final DataWatcher.Item<?> item = (DataWatcher.Item<?>) element;
										// a() - getWatcherObject
										// a(Object) - setValue
										// a(boolean) - setFlag ?
										// b() - getValue
										// c() - getFlag ?
										// d() - clone
										System.out.println("> [" + item.a().a() + "] > " + item.b().getClass() + " = " + item.b().toString());
									} else {
										System.out.println("> [*] > " + element.getClass() + " = " + element.toString());
									}
								}
								System.out.println("> }");
							} else {
								System.out.println("> " + object.getClass() + " = " + object.toString());
							}
						} else {
							System.out.println("> null");
						}
					}*//*
				}
			});*/

			initialized = true;
		}
	}

	static Hologram register(Hologram hologram) {
		if(!initialized) {
			throw new IllegalMonitorStateException("InventoryInterfaceManager was not initialized by any plugin!");
		} else {
			holograms.add(hologram);
			return hologram;
		}
	}

	void update() {
		for(Hologram hologram : holograms) {
			hologram.update();
		}
	}
}
