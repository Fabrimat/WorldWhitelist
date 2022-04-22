package eu.k129.fabrimat.worldwhitelist.commands;

import eu.k129.fabrimat.worldwhitelist.config.ConfigManager;
import eu.k129.fabrimat.worldwhitelist.WorldWhitelist;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhitelistCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(WorldWhitelist.getInstance(), () -> {
            ConfigManager configManager = ConfigManager.get();
    
            if(args.length > 0) {
                World world = Bukkit.getWorld(args[0]);
                String worldName = args[0];
                if(world != null) {
                    switch (args[1].toLowerCase()) {
                        case "on":
                            if(!commandSender.hasPermission("worldwhitelist.world." + worldName + ".on")) {
                                commandSender.sendMessage(configManager.getMessage("no_permission"));
                                return;
                            }
                            
                            if(!configManager.isWorldWhitelisted(worldName)) {
                                configManager.setWorldWhitelisted(worldName, true);
                                commandSender.sendMessage(String.format(configManager.getMessage("whitelist_enabled"), worldName));
                            } else {
                                commandSender.sendMessage(String.format(configManager.getMessage("whitelist_already_enabled"), worldName));
                            }
                            break;
                        case "off":
                            if(!commandSender.hasPermission("worldwhitelist.world." + worldName + ".off")) {
                                commandSender.sendMessage(configManager.getMessage("no_permission"));
                                return;
                            }
                            if(configManager.isWorldWhitelisted(worldName)) {
                                configManager.setWorldWhitelisted(worldName, false);
                                commandSender.sendMessage(String.format(configManager.getMessage("whitelist_disabled"), worldName));
                            } else {
                                commandSender.sendMessage(String.format(configManager.getMessage("whitelist_already_disabled"), worldName));
                            }
                            break;
                        case "add":
                            if(!commandSender.hasPermission("worldwhitelist.world." + worldName + ".add")) {
                                commandSender.sendMessage(configManager.getMessage("no_permission"));
                                return;
                            }
                            if(args.length == 3) {
                                String playerName = args[2];
                                if(!configManager.isUserWhitelisted(playerName, worldName)) {
                                    configManager.setUserWhitelisted(playerName, worldName, true);
                                    commandSender.sendMessage(String.format(configManager.getMessage("user_added"), worldName, playerName));
                                } else {
                                    commandSender.sendMessage(String.format(configManager.getMessage("user_already_added"), worldName, playerName));
                                }
                            } else {
                                commandSender.sendMessage(configManager.getMessage("user_not_specified"));
                            }
                            break;
                        case "remove":
                            if(!commandSender.hasPermission("worldwhitelist.world." + worldName + ".remove")) {
                                commandSender.sendMessage(configManager.getMessage("no_permission"));
                                return;
                            }
                            if(args.length == 3) {
                                String playerName = args[2];
                                if(configManager.isUserWhitelisted(playerName, worldName)) {
                                    configManager.setUserWhitelisted(playerName, worldName, false);
                                    commandSender.sendMessage(String.format(configManager.getMessage("user_removed"), worldName, playerName));
                                } else {
                                    commandSender.sendMessage(String.format(configManager.getMessage("user_already_removed"), worldName, playerName));
                                }
                            } else {
                                commandSender.sendMessage(configManager.getMessage("user_not_specified"));
                            }
                            break;
                        case "list":
                            if(!commandSender.hasPermission("worldwhitelist.world." + worldName + ".list")) {
                                commandSender.sendMessage(configManager.getMessage("no_permission"));
                                return;
                            }
                            List<String> uuidList = configManager.getWhitelistedUsers(worldName);
                            List<String> usersList = new ArrayList<>();
                            for(String uuidString : uuidList) {
                                UUID uuid = UUID.fromString(uuidString);
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                                usersList.add(offlinePlayer.getName());
                            }
                            String usersString = String.join(", ", usersList);
                            commandSender.sendMessage(String.format(configManager.getMessage("whitelist_list"), worldName, usersString));
                            break;
                        default:
                            commandSender.sendMessage(configManager.getMessage("command_not_valid"));
                            break;
                    }
                } else {
                    commandSender.sendMessage(String.format(configManager.getMessage("world_not_valid"), worldName));
                }
            }
        });
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        ConfigManager configManager = ConfigManager.get();
        List<String> tempValues = new ArrayList<>();
        List<String> returnValues = new ArrayList<>();
        if(args.length == 1) {
            for(World world : Bukkit.getWorlds()) {
                tempValues.add(world.getName());
            }
            StringUtil.copyPartialMatches(args[0], tempValues, returnValues);
        } else if(args.length == 2) {
            tempValues.add("on");
            tempValues.add("off");
            tempValues.add("add");
            tempValues.add("remove");
            tempValues.add("list");
            StringUtil.copyPartialMatches(args[1], tempValues, returnValues);
        } else if(args.length == 3) {
            switch (args[1]) {
                case "add":
                    for(Player player: Bukkit.getOnlinePlayers()) {
                        tempValues.add(player.getName());
                    }
                    break;
                case "remove":
                    List<String> uuidList = configManager.getWhitelistedUsers(args[0]);
                    for(String uuidString : uuidList) {
                        UUID uuid = UUID.fromString(uuidString);
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                        tempValues.add(offlinePlayer.getName());
                    }
                    break;
            }
            StringUtil.copyPartialMatches(args[2], tempValues, returnValues);
        }
        return returnValues;
    }
}
