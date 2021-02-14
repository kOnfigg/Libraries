package net.hodyus;

import net.hodyus.libsplugin.storage.DatabaseConnector;
import org.bukkit.plugin.java.JavaPlugin;

public class LibsPlugin extends JavaPlugin {

    private DatabaseConnector database;

    public static LibsPlugin getInstance() {
        return JavaPlugin.getPlugin(LibsPlugin.class);
    }

    public DatabaseConnector getDatabaseConnector() {
        return database;
    }

    public void onEnable() {
        System.out.println("Ativando libraries...");

        database = new DatabaseConnector("jdbc:mysql://localhost:3306/testes",
                "admin",
                "");

        database.connect();
    }

}
