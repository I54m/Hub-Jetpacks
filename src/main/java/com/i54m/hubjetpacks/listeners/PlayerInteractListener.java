package com.i54m.hubjetpacks.listeners;

import com.i54m.hubjetpacks.HubJetpacks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class PlayerInteractListener implements Listener {

    private final HubJetpacks plugin = HubJetpacks.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand() != null)
                if (player.getInventory().getItemInMainHand().isSimilar(plugin.getEngageItem()))
                    plugin.getJetpack(player).engage();
                else if (player.getInventory().getItemInMainHand().isSimilar(plugin.getDisengageItem()))
                    plugin.getJetpack(player).disengage();
        }
    }
}
