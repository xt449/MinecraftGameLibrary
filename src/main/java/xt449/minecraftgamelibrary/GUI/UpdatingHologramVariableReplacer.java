package xt449.minecraftgamelibrary.GUI;

public class UpdatingHologramVariableReplacer {
	final String placeholder;
	final UpdatingHologramVariable variable;

	public UpdatingHologramVariableReplacer(String placeholder, UpdatingHologramVariable variable) {
		this.placeholder = placeholder;
		this.variable = variable;
	}

	String parse(String text, Hologram hologram) {
		return text.replace(placeholder, variable.getValue(hologram));
	}
}
