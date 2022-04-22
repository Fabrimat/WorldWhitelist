package eu.k129.fabrimat.worldwhitelist.config;

import eu.k129.fabrimat.worldwhitelist.WorldWhitelist;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.UUID;

public class ConfigManager {
    
    private WorldWhitelist plugin = WorldWhitelist.getInstance();
    
    public static ConfigManager get() {
        return new ConfigManager();
    }
    
    public String getDefaultWorld() {
        return plugin.getConfig().getString("default_world", "world");
    }
    
    public List<String> getWhitelistedWorlds() {
        return plugin.getConfig().getStringList("whitelist_worlds");
    }
    
    public boolean isWorldWhitelisted(String world) {
        List<String> worlds = plugin.getConfig().getStringList("whitelist_worlds");
        for(String loopWorld : worlds) {
            if(loopWorld.equals(world)) {
                return true;
            }
        }
        return false;
    }
    
    public List<String> getWhitelistedUsers(String world) {
        return plugin.getConfig().getStringList("users." + world);
    }
    
    private void setWhitelistedUsers(String world, List<String> players) {
        if(world != null) {
            plugin.getConfig().set("users." + world, players);
        }
        plugin.saveConfig();
    }
    
    public boolean isUserWhitelisted(String player, String world) {
        UUID userUUID = Bukkit.getOfflinePlayer(player).getUniqueId();
        return this.isUserWhitelisted(userUUID, world);
    }
    
    public boolean isUserWhitelisted(UUID playerUUID, String world) {
        List<String> users = plugin.getConfig().getStringList("users." + world);
        String userUUID = playerUUID.toString();
        for(String loopUser : users) {
            if(loopUser.equals(userUUID)) {
                return true;
            }
        }
        return false;
    }
    
    public void setWorldWhitelisted(String world, boolean whitelisted) {
        boolean worldWhitelisted = this.isWorldWhitelisted(world);
        
        List<String> worlds = plugin.getConfig().getStringList("whitelist_worlds");
        if(whitelisted && !worldWhitelisted) {
            worlds.add(world);
        } else if(!whitelisted && worldWhitelisted) {
            worlds.removeIf(loopWorld -> loopWorld.equals(world));
        }
        plugin.getConfig().set("whitelist_worlds", worlds);
        plugin.saveConfig();
    }
    
    public void setUserWhitelisted(String player, String world, boolean whitelist) {
        UUID userUUID = Bukkit.getOfflinePlayer(player).getUniqueId();
        this.setUserWhitelisted(userUUID, world, whitelist);
    }
    
    public void setUserWhitelisted(UUID playerUUID, String world, boolean whitelist) {
        boolean userWhitelisted = this.isUserWhitelisted(playerUUID, world);
        if (whitelist && !userWhitelisted) {
            List<String> users = this.getWhitelistedUsers(world);
            String userUUID = playerUUID.toString();
            users.add(userUUID);
            this.setWhitelistedUsers(world, users);
        }
        
        if(!whitelist && userWhitelisted) {
            List<String> users = this.getWhitelistedUsers(world);
            String userUUID = playerUUID.toString();
            users.removeIf(loopUser -> loopUser.equals(userUUID));
            this.setWhitelistedUsers(world, users);
        }
        plugin.saveConfig();
    }
    
    public String getMessage(String message) {
        String value = plugin.getConfig().getString("message." + message, null);
        if(value != null) {
            value = plugin.getConfig().getString("message.prefix", "&7[&6WorldWhitelist&7]&r ") + value;
            value = ChatColor.translateAlternateColorCodes('&', value);
        } else {
            value = "message." + message;
        }
        return value;
    }
    
    public void reload() {
        plugin.reloadConfig();
    }
}
