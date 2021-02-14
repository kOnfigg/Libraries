package net.hodyus.libsplugin.text;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PermissionAPI {

    static RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
    static LuckPerms api = provider.getProvider();

    public static String getPrefix(String name){
        User user = api.getUserManager().getUser(name);
        if (user == null) return "§7";
        String prefix = user.getCachedData().getMetaData().getPrefix().replaceAll("&", "§");
        return prefix;
    }

}
