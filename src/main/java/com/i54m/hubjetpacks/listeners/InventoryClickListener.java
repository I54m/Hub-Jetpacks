package com.i54m.hubjetpacks.listeners;

import com.i54m.hubjetpacks.HubJetpacks;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private final HubJetpacks plugin = HubJetpacks.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().isSimilar(plugin.getJetpack())) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(ChatColor.RED + "Don't remove your jetpack! How would you get back to the station if you went floating off into space?!?");
            } else if (event.getCurrentItem().isSimilar(plugin.getEngageItem()) ||
                    event.getCurrentItem().isSimilar(plugin.getDisengageItem()) ||
                    event.getCurrentItem().isSimilar(plugin.getOutOfFuelItem()) ||
                    event.getCurrentItem().isSimilar(plugin.getCooldownItem())) {
                event.setCancelled(true);
            } else if (event.getCurrentItem().isSimilar(plugin.getBouncyBoots())) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(ChatColor.RED + "Don't remove your bouncy boots! You may break your legs if you land too hard!!");
            } else if (event.getCurrentItem().isSimilar(plugin.getSpaceSuitLegs())) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(ChatColor.RED + "Don't remove your space suit leggings! You may freeze to death and that's a lot of paper work!!");
            }
        }
    }
}
