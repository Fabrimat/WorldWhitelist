package eu.k129.fabrimat.worldwhitelist.listeners;

import eu.k129.fabrimat.worldwhitelist.config.ConfigManager;
import eu.k129.fabrimat.worldwhitelist.WorldWhitelist;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class WorldListener implements Listener {
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        onWorldEnter(event.getPlayer());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        onWorldEnter(event.getPlayer());
    }
    
    private void onWorldEnter(Player player) {
        ConfigManager configManager = ConfigManager.get();
        String worldName = player.getWorld().getName();
        
        if(!worldName.equals(configManager.getDefaultWorld()) && !player.hasPermission("worldwhitelist." + worldName + ".bypass")) {
            World defaultWorld = Bukkit.getWorld(configManager.getDefaultWorld());
            if(!configManager.isUserWhitelisted(player.getUniqueId(), worldName)) {
                if(defaultWorld != null) {
                    Bukkit.getScheduler().runTaskAsynchronously(WorldWhitelist.getInstance(),
                            () -> Bukkit.getScheduler().runTask(WorldWhitelist.getInstance(),
                                    () -> player.teleport(defaultWorld.getSpawnLocation()
                                    )
                            )
                    );
                    player.sendMessage(configManager.getMessage("no_access_world"));
                } else {
                    player.kickPlayer(configManager.getMessage("no_access_world"));
                }
            }
        }
    }
}
