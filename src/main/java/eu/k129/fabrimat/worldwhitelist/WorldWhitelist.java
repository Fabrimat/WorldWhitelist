package eu.k129.fabrimat.worldwhitelist;

import eu.k129.fabrimat.worldwhitelist.commands.WhitelistCommand;
import eu.k129.fabrimat.worldwhitelist.commands.WhitelistReloadCommand;
import eu.k129.fabrimat.worldwhitelist.listeners.WorldListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class WorldWhitelist extends JavaPlugin {
    
    public static WorldWhitelist getInstance() {
        return JavaPlugin.getPlugin(WorldWhitelist.class);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        this.getCommand("worldwhitelist").setExecutor(new WhitelistCommand());
        this.getCommand("worldwhitelist").setTabCompleter(new WhitelistCommand());
        this.getCommand("worldwhitelistreload").setExecutor(new WhitelistReloadCommand());
        getLogger().log(Level.INFO, "WorldWhitelist has been enabled!");
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().log(Level.INFO, "WorldWhitelist has been disabled!");
    }
}
