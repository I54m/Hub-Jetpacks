package com.i54m.hubjetpacks.listeners;

import com.i54m.hubjetpacks.HubJetpacks;
import com.i54m.hubjetpacks.Jetpack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final HubJetpacks plugin = HubJetpacks.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event){
        plugin.getJetpacks().put(event.getPlayer(), new Jetpack(event.getPlayer()));
    }
}
