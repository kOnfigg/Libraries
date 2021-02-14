package net.hodyus.libsplugin.text;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ActionBar {
	
	public ActionBar(Player p, String msg, Sound sound) {
		PacketPlayOutChat action = new PacketPlayOutChat(new ChatComponentText(msg), (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(action);
		if (sound != null) {
			p.playSound(p.getLocation(), sound, 1.F, 1.F);
		}
	}
	
}
