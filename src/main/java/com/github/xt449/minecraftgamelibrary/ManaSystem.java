/*
 * Copyright (c) 2020 xt449/BinaryBanana
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

import org.bukkit.entity.Player;

@Deprecated
public class ManaSystem {

	final Checker checker;
	final Spender spender;

	final ManaFailure manaFailure;
	final CooldownFailure cooldownFailure;

	public ManaSystem(Checker checker, Spender spender, ManaFailure manaFailure, CooldownFailure cooldownFailure) {
		this.checker = checker;
		this.spender = spender;

		this.manaFailure = manaFailure;
		this.cooldownFailure = cooldownFailure;
	}

	@FunctionalInterface
	public static interface Checker {
		boolean check(Spell spell, Player player, int mana);
	}

	@FunctionalInterface
	public static interface Spender {
		void spend(Spell spell, Player player, int mana);
	}

	@FunctionalInterface
	public static interface ManaFailure {
		void execute(Spell spell, Player player);
	}

	@FunctionalInterface
	public static interface CooldownFailure {
		void execute(Spell spell, Player player);
	}
}
