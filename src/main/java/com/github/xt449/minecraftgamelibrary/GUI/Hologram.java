package com.github.xt449.minecraftgamelibrary.GUI;

import net.minecraft.server.v1_15_R1.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.*;

/**
 * @author xt449
 */
public class Hologram {

	private Location locationBase;

	private List<String> rawTexts;
	private List<UpdatingHologramVariableReplacer> replacers;

	//private boolean defaultVisibility;
	//private Collection<Player> playerList;

	private List<ArmorStand> parts = new ArrayList<>();

	//private String[] texts;

	Hologram(Location locationBase, List<String> rawTexts, List<UpdatingHologramVariableReplacer> replacers/*, boolean defaultVisibility, Collection<Player> playerList*/) {
		this.locationBase = locationBase;
		this.rawTexts = rawTexts;
		this.replacers = replacers;
		//this.defaultVisibility = defaultVisibility;
		//this.playerList = playerList;

		update();
	}

	/*boolean isVisibleTo(Player player) {
		return defaultVisibility != playerList.contains(player);
	}*/

	public Location debug() {
		System.out.println(parts.size() + " parts at 0,0 terrain");

		for(ArmorStand armorStand : parts) {
			armorStand.teleport(armorStand.getLocation().add(-1, -1, -1));
			armorStand.setCustomName("OMEGALUL?");
			armorStand.setCustomNameVisible(true);
			armorStand.setMarker(false);
			((CraftArmorStand) armorStand).getHandle().setInvisible(false);
			armorStand.teleport(armorStand.getLocation().add(2, 2, 2));
			//System.out.println(armorStand);
			//System.out.println("Invulnerable: " + armorStand.isInvulnerable());
			//System.out.println("Alive: " + !armorStand.isDead());
		}

		return parts.get(0).getLocation();
	}

	void update() {
		while(parts.size() < rawTexts.size()) {
			ArmorStand armorStand = null;

			try {
				armorStand = (ArmorStand) locationBase.getWorld().spawnEntity(locationBase, EntityType.ARMOR_STAND);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			if(armorStand == null) {
				System.out.println("BOGGED!");
				continue;
			}

			/*while(armorStand == null) {
				armorStand = (ArmorStand) locationBase.getWorld().spawnEntity(locationBase, EntityType.ARMOR_STAND);
				TimeUnit.SECONDS.sleep(1);
			}*/
			{
				// 1.13 - armorStand.setCanMove(false);
				// 1.13 - armorStand.setCanTick(false);
				armorStand.setAI(false);
			}
			{
				EntityArmorStand entityArmorStand = ((CraftArmorStand) armorStand).getHandle();
				entityArmorStand.setCustomNameVisible(true);
				entityArmorStand.setInvisible(true);
				entityArmorStand.setInvulnerable(true);
				entityArmorStand.setMarker(true);
				entityArmorStand.setNoGravity(true);
			}

			parts.add(armorStand);
		}

		while(parts.size() > rawTexts.size()) {
			parts.remove(parts.size() - 1);
		}

		Location location = locationBase.clone();

		for(int i = rawTexts.size() - 1; i >= 0; i--) {
			ArmorStand armorStand = parts.get(i);

			armorStand.teleport(location);
			location.add(0, 0.4, 0);

			String text = rawTexts.get(i);
			for(UpdatingHologramVariableReplacer replacer : replacers) {
				text = replacer.parse(text, this);
			}

			armorStand.setCustomName(text);
		}
	}
}
