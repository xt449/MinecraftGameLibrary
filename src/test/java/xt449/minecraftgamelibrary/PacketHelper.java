package xt449.minecraftgamelibrary;

public class PacketHelper {

	private static int nextEntityID = -3;

	public static int getNextEntityID() {
		return nextEntityID -= 2;
	}
}
