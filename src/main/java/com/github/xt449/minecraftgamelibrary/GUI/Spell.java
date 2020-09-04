package com.github.xt449.minecraftgamelibrary.GUI;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Function;

public class Spell {

	private final HashMap<Player, Integer> playerCooldowns = new HashMap<>();

	public final String name;
	public final Material material;
	public final int mana;
	public final int cooldown;
	private final Function<Player, Boolean> action;
	private final Consumer<Player> onFailCooldown;
	private final Consumer<Player> onFailMana;

	/**
	 * @param mana     cost in experience levels
	 * @param cooldown in ticks
	 */
	public Spell(@NotNull String name, @NotNull Material material, int mana, int cooldown, @NotNull Function<Player, Boolean> action, @NotNull Consumer<Player> onFailCooldown, @NotNull Consumer<Player> onFailMana) {
		this.name = name;
		this.material = material;
		this.mana = mana;
		this.cooldown = cooldown;
		this.action = action;
		this.onFailCooldown = onFailCooldown;
		this.onFailMana = onFailMana;
	}

	/**
	 * @param mana     cost in experience levels
	 * @param cooldown in ticks
	 */
	public Spell(@NotNull String name, @NotNull Material material, int mana, int cooldown, @NotNull Function<Player, Boolean> action) {
		this.name = name;
		this.material = material;
		this.mana = mana;
		this.cooldown = cooldown;
		this.action = action;
		this.onFailCooldown = Spell::emptyPlayerConsumer;
		this.onFailMana = Spell::emptyPlayerConsumer;
	}

//	/**
//	 * @param mana     cost in experience levels
//	 * @param cooldown in ticks
//	 */
//	public Spell(String name, Material material, int mana, int cooldown, Function<Player, Boolean> action) {
//		this.name = name;
//		this.material = material;
//		this.mana = mana;
//		this.cooldown = cooldown;
//		this.action = action;
//	}
//
//	/**
//	 * @param mana     cost in experience levels
//	 * @param cooldown in ticks
//	 */
//	public Spell(String name, Material material, int mana, int cooldown, Function<Player, Boolean> action) {
//		this.name = name;
//		this.material = material;
//		this.mana = mana;
//		this.cooldown = cooldown;
//		this.action = action;
//	}

	public void cast(@NotNull Player player) {
		if(!onCooldown(player)) {
			if(manaChecker(player, this.mana)) {
				if(this.action.apply(player)) {
					startCooldown(player);
					manaSpender(player, this.mana);
				}
			} else {
				onFailMana.accept(player);
				final TextComponent text = new TextComponent("Not enough mana");
				text.setColor(ChatColor.DARK_AQUA);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
			}
		} else {
			onFailCooldown.accept(player);
			final TextComponent text = new TextComponent("Cooldown has " + decimalFormat.format(getRemainingCooldown(player) / 20F) + " seconds remaining");
			text.setColor(ChatColor.DARK_AQUA);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
		}
	}

	public final void register() {
		Spell.registered.add(this);
	}

	public final void registerInteractiveLeftClick() {
		Spell.registered.add(this);
		Spell.registeredInteractiveLeftClick.put(this.material, this);
	}

	public final void registerInteractiveRightClick() {
		Spell.registered.add(this);
		Spell.registeredInteractiveRightClick.put(this.material, this);
	}

	public int getRemainingCooldown(Player player) {
		final Integer remainingCooldown = this.playerCooldowns.get(player);
		return remainingCooldown != null ? remainingCooldown : 0;
	}

	public boolean onCooldown(Player player) {
		final Integer remainingCooldown = this.playerCooldowns.get(player);
		return remainingCooldown != null && remainingCooldown > 0;
	}

	private void startCooldown(Player player) {
		this.playerCooldowns.put(player, this.cooldown);
	}

//	private void tickCooldowns() {
//		for(Map.Entry<Player, Integer> kvp : this.playerCooldowns.entrySet()) {
//			if(kvp.getValue() != null && kvp.getValue() > 0) {
//				this.playerCooldowns.put(kvp.getKey(), kvp.getValue() - 1);
//			}
//		}
//		/*for(Player player : this.playerCooldowns.keySet()) {
//			tickCooldown(player);
//		}*/
//	}

//	private void tickCooldown(Player player) {
//		final Integer remainingCooldown = this.playerCooldowns.get(player);
//		if(remainingCooldown != null && remainingCooldown > 0) {
//			this.playerCooldowns.put(player, remainingCooldown - 1);
//		}
//		/*if(remainingCooldown != null) {
//			if(remainingCooldown > 0) {
//				this.playerCooldowns.put(player, remainingCooldown - 1);
//			}
//		} else {
//			this.playerCooldowns.put(player, 0);
//		}*/
//	}

	// static

	private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

	private static final HashSet<Spell> registered = new HashSet<>();
	private static final HashMap<Material, Spell> registeredInteractiveLeftClick = new HashMap<>();
	private static final HashMap<Material, Spell> registeredInteractiveRightClick = new HashMap<>();

	public static HashSet<Spell> getAll() {
		return Spell.registered;
	}

	public static Spell getByInteractiveLeftClickMaterial(Material material) {
		return Spell.registeredInteractiveLeftClick.get(material);
	}

	public static Spell getByInteractiveRightClickMaterial(Material material) {
		return Spell.registeredInteractiveRightClick.get(material);
	}

	public static void tickAllCooldowns() {
		for(Spell spell : Spell.registered) {
			for(HashMap.Entry<Player, Integer> kvp : spell.playerCooldowns.entrySet()) {
				if(kvp.getValue() != null && kvp.getValue() > 0) {
					spell.playerCooldowns.put(kvp.getKey(), kvp.getValue() - 1);
				}
			}
			/*spell.tickCooldowns();*/
		}
	}

	private static void emptyPlayerConsumer(Player player) {

	}

	private static boolean manaChecker(Player player, int mana) {
		return mana <= player.getLevel();
	}

	private static void manaSpender(Player player, int mana) {
		final int remaining = player.getLevel() - mana;
		if(remaining < 0) {
			player.setLevel(0);
		} else {
			player.setLevel(remaining);
		}
	}
}
