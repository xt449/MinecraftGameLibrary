package com.github.xt449.minecraftgamelibrary.GUI;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xt449
 */
public class Hologram {

	private final Location locationBase;

	private final List<String> rawTexts;
	private final List<UpdatingHologramVariableReplacer> replacers;

	//private boolean defaultVisibility;
	//private Collection<Player> playerList;

	private final List<ArmorStand> parts = new ArrayList<>();

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

	void update() {
		while(parts.size() < rawTexts.size()) {
			ArmorStand armorStand = null;

			try {
				armorStand = (ArmorStand) locationBase.getWorld().spawnEntity(locationBase, EntityType.ARMOR_STAND);
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			if(armorStand == null) {
				continue;
			}

			/*while(armorStand == null) {
				armorStand = (ArmorStand) locationBase.getWorld().spawnEntity(locationBase, EntityType.ARMOR_STAND);
				TimeUnit.SECONDS.sleep(1);
			}*/

			// 1.13 - armorStand.setCanMove(false);
			// 1.13 - armorStand.setCanTick(false);
			armorStand.setAI(false);
			armorStand.setCustomNameVisible(true);
			armorStand.setInvulnerable(true);
			armorStand.setMarker(true);
			armorStand.setGravity(false);

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
