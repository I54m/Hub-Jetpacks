package com.i54m.hubjetpacks.listeners;

import com.i54m.hubjetpacks.HubJetpacks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKickListener implements Listener {

    private final HubJetpacks plugin = HubJetpacks.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        if (plugin.getJetpacks().get(event.getPlayer()).isEngaged() && event.getReason().toLowerCase().contains("flying is not enabled on this server")) {
            event.setCancelled(true);
            plugin.getLogger().warning("Server tried to kick player while they were using a jetpack, this plugin makes it so players can use fly while using jetpacks, to prevent this issue (but stops them flying if they try), something went wrong and the player was denied allowFlight (/fly). Attempted to stop playing being kicked, but server may override.");
        }
    }
}
