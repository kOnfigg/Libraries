package net.hodyus.libsplugin.command;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

public class CommandRegister {
	
	public CommandRegister(Command command, String plugin) {
		try {
			Field cmap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			cmap.setAccessible(true);
			CommandMap map = (CommandMap)cmap.get(Bukkit.getServer());
			map.register(plugin, command);
			System.out.println("Comando: " + command.getName() + " Registrado!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problema ao registrar o comando: " + command.getName() + ".");
		}
	}
	
}
