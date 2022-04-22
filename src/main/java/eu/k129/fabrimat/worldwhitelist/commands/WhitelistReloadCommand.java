package eu.k129.fabrimat.worldwhitelist.commands;

import eu.k129.fabrimat.worldwhitelist.config.ConfigManager;
import eu.k129.fabrimat.worldwhitelist.WorldWhitelist;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WhitelistReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ConfigManager configManager = ConfigManager.get();
        if(!commandSender.hasPermission("worldwhitelist.admin.reload")) {
            commandSender.sendMessage(configManager.getMessage("no_permission"));
            return true;
        }
        
        Bukkit.getScheduler().runTaskAsynchronously(WorldWhitelist.getInstance(), () -> {
            configManager.reload();
            commandSender.sendMessage(configManager.getMessage("config_reloaded"));
        });
        return true;
    }
}
