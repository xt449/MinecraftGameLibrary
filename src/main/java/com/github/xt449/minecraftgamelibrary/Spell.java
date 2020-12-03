/*
 * Copyright (c) 2020 Jonathan Talcott (xt449 / BinaryBanana)
 *
 * This file is part of MinecraftGameLibrary.
 *
 * MinecraftGameLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MinecraftGameLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MinecraftGameLibrary.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.github.xt449.minecraftgamelibrary;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author Jonathan Taclott (xt449 / BinaryBanana)
 */
public class Spell {

	private final HashMap<Player, Integer> playerCooldowns = new HashMap<>();

	public final String name;
	public final Material material;
	public final int mana;
	public final int cooldown;
	protected final Action action;
	protected final ChatColor chatColor;

	/**
	 * @param name      name for itemstack
	 * @param material  material for itemstack
	 * @param mana      mana cost
	 * @param cooldown  cooldown in ticks
	 * @param action    action on cast
	 */
	public Spell(@NotNull String name, @NotNull Material material, int mana, int cooldown, @NotNull Action action) {
		this.name = name;
		this.material = material;
		this.mana = mana;
		this.cooldown = cooldown;
		this.action = action;
		this.chatColor = ChatColor.WHITE;
	}

	/**
	 * @param name      name for itemstack
	 * @param material  material for itemstack
	 * @param mana      mana cost
	 * @param cooldown  cooldown in ticks
	 * @param action    action on cast
	 * @param chatColor color for itemstack name
	 */
	public Spell(@NotNull String name, @NotNull Material material, int mana, int cooldown, @NotNull Action action, @NotNull ChatColor chatColor) {
		this.name = name;
		this.material = material;
		this.mana = mana;
		this.cooldown = cooldown;
		this.action = action;
		this.chatColor = chatColor;
	}

	protected void startCooldown(@NotNull Player player) {
		this.playerCooldowns.put(player, this.cooldown);
	}

	@Deprecated
	protected void tickCooldowns() {
		for(HashMap.Entry<Player, Integer> kvp : this.playerCooldowns.entrySet()) {
			if(kvp.getValue() != null && kvp.getValue() > 0) {
				this.playerCooldowns.put(kvp.getKey(), kvp.getValue() - 1);
			}
		}
	}

	@Deprecated
	protected void tickCooldown(@NotNull Player player) {
		final Integer remainingCooldown = this.playerCooldowns.get(player);
		if(remainingCooldown != null && remainingCooldown > 0) {
			this.playerCooldowns.put(player, remainingCooldown - 1);
		}
	}

	/**
	 * Attempts to cast the spell as the given player
	 */
	public void cast(@NotNull Player player) {
		if(!onCooldown(player)) {
			if(checkMana(player)) {
				if(this.action.apply(player)) {
					startCooldown(player);
					spendMana(player);
				}
			} else {
				onManaFailure(player);
				final TextComponent text = new TextComponent("Not enough mana");
				text.setColor(ChatColor.DARK_AQUA);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
			}
		} else {
			onCooldownFailure(player);
			final TextComponent text = new TextComponent("Cooldown has " + Spell.decimalFormat.format(getRemainingCooldown(player) / 20F) + " seconds remaining");
			text.setColor(ChatColor.DARK_AQUA);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
		}
	}

	/**
	 * Register this spell for cooldown ticking via {@link Spell#tickAllCooldowns()}
	 */
	public final void register() {
		Spell.registered.add(this);
	}

	/**
	 * @param player caster
	 * @return remaining cooldown in ticks for given player
	 */
	public int getRemainingCooldown(@NotNull Player player) {
		final Integer remainingCooldown = this.playerCooldowns.get(player);
		return remainingCooldown != null ? remainingCooldown : 0;
	}

	/**
	 * @param player caster
	 * @return true if cooldown is active for given player
	 */
	public boolean onCooldown(@NotNull Player player) {
		final Integer remainingCooldown = this.playerCooldowns.get(player);
		return remainingCooldown != null && remainingCooldown > 0;
	}

	/**
	 * @return item stack with instance's name, material, and color
	 */
	public ItemStack getItemStack() {
		final ItemStack itemStack = new ItemStack(material);
		final ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(chatColor + name);
		itemMeta.setLore(Arrays.asList(org.bukkit.ChatColor.DARK_AQUA + "Mana: " + mana, org.bukkit.ChatColor.RED + "Cooldown: " + (cooldown / 20F) + " seconds"));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(other == null || getClass() != other.getClass()) return false;
		final Spell spell = (Spell) other;
		return name.equals(spell.name) &&
				material == spell.material &&
				mana == spell.mana &&
				cooldown == spell.cooldown &&
				action.equals(spell.action);
	}

	/**
	 * Returns true if the given player has enough "mana" to cast this spell
	 */
	public boolean checkMana(Player player) {
		return mana <= player.getLevel();
	}

	/**
	 * Reduces the "mana" of the given player by the cost of this spell
	 */
	public void spendMana(Player player) {
		player.setLevel(Math.max(player.getLevel() - mana, 0));
	}

	/**
	 * Called when a spell cast attempt fails because of a lack of "mana"
	 */
	public void onManaFailure(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.2F, 1);
		final TextComponent text = new TextComponent("Requires " + mana + " mana");
		text.setColor(ChatColor.DARK_AQUA);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
	}

	/**
	 * Called when a spell cast attempt fails because of the cooldown still being active
	 */
	public void onCooldownFailure(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 0.2F, 1);
		final TextComponent text = new TextComponent("Cooldown has " + Spell.decimalFormat.format(getRemainingCooldown(player) / 20F) + " seconds remaining");
		text.setColor(ChatColor.DARK_AQUA);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
	}

	// static

	private static final HashSet<Spell> registered = new HashSet<>();

	/**
	 * Gets a set of all of the registered spells
	 */
	public static HashSet<Spell> getAll() {
		return Spell.registered;
	}

	protected static DecimalFormat decimalFormat = new DecimalFormat("#.##");

	/**
	 * Sets the decimal format to use for display
	 */
	public static void setDecimalFormat(DecimalFormat decimalFormat) {
		Spell.decimalFormat = decimalFormat;
	}

	/**
	 * Ticks all registered spells' cooldowns
	 */
	public static void tickAllCooldowns() {
		for(Spell spell : Spell.registered) {
			for(HashMap.Entry<Player, Integer> kvp : spell.playerCooldowns.entrySet()) {
				if(kvp.getValue() != null && kvp.getValue() > 0) {
					spell.playerCooldowns.put(kvp.getKey(), kvp.getValue() - 1);
				}
			}
		}
	}

	@FunctionalInterface
	public static interface Action {
		/**
		 * @param player spell caster of action
		 * @return true if spell action casts successfully
		 */
		boolean apply(Player player);
	}
}
