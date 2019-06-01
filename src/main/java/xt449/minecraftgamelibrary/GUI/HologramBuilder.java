package xt449.minecraftgamelibrary.GUI;

import org.bukkit.Location;

import java.util.*;

public class HologramBuilder {

	private List<String> rawTexts = new ArrayList<>();
	private List<UpdatingHologramVariableReplacer> replacers = new ArrayList<>();
	//private boolean defaultVisibility = false;
	//private List<Player> playerList = new ArrayList<>();

	public HologramBuilder(String rawTexts) {
		this.rawTexts.addAll(Arrays.asList(rawTexts.split("\n")));
		/*for(String rawText : rawTexts.split("\n")) {
			this.rawTexts.add(rawText);
		}*/
	}

	public HologramBuilder(List<String> rawTexts) {
		this.rawTexts = rawTexts;
	}

	public HologramBuilder setRawTexts(List<String> rawTexts) {
		this.rawTexts = rawTexts;

		return this;
	}

	public HologramBuilder addText(String text) {
		this.rawTexts.add(text);

		return this;
	}

	public HologramBuilder setReplacers(List<UpdatingHologramVariableReplacer> replacers) {
		this.replacers = replacers;

		return this;
	}

	public HologramBuilder addReplacer(UpdatingHologramVariableReplacer replacer) {
		this.replacers.add(replacer);

		return this;
	}

	/*public HologramBuilder setDefaultVisibility(boolean defaultVisibility) {
		this.defaultVisibility = defaultVisibility;

		return this;
	}

	public HologramBuilder setPlayerList(List<Player> playerList) {
		this.playerList = playerList;

		return this;
	}

	public HologramBuilder addPlayer(Player player) {
		this.playerList.add(player);

		return this;
	}*/

	/*public Hologram build(World world, int x, int y, int z) {
		return build(new Location(world, x, y, z));
	}*/

	public Hologram build(Location location) {
		return HologramManager.register(new Hologram(location, rawTexts, replacers/*, defaultVisibility, playerList*/));
	}
}
