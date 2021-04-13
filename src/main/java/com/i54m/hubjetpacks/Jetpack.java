package com.i54m.hubjetpacks;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Jetpack {
    private final HubJetpacks plugin = HubJetpacks.getInstance();
    @Getter
    private final BossBar bossBar = Bukkit.createBossBar(ChatColor.GREEN + "Jetpack ready!", BarColor.YELLOW, BarStyle.SEGMENTED_10);
    @Getter
    private boolean engaged = false;
    @Getter
    @Setter
    private boolean outOfFuel = false;
    @Getter
    private boolean fuelingCooldown = false;
    @Getter
    private final Player owner;

    public Jetpack(Player owner) {
        owner.getInventory().setChestplate(plugin.getJetpack());
        owner.getInventory().setBoots(plugin.getBouncyBoots());
        owner.getInventory().setLeggings(plugin.getSpaceSuitLegs());
        owner.getInventory().setItem(plugin.getConfig().getInt("EngageItemslot", 8), plugin.getEngageItem());
        owner.updateInventory();
        bossBar.setProgress(1.0);
        bossBar.addPlayer(owner);
        this.owner = owner;
    }

    public void engage() {
        engaged = true;
        if (owner.getLocation().subtract(0, 1, 0).getBlock().getType().isSolid()) {
            owner.playSound(owner.getLocation(), Sound.ENTITY_BLAZE_SHOOT, .5f, 1);
            owner.setVelocity(owner.getLocation().getDirection().add(new Vector(0, 3, 0)));
            owner.spawnParticle(Particle.REDSTONE, owner.getLocation().add(0, 0.75, 0), 5, 0.12, 0.25, 0.12, new Particle.DustOptions(Color.fromRGB(250, 100, 0), 1));
            owner.spawnParticle(Particle.REDSTONE, owner.getLocation().add(0, -0.5, 0), 15, 0.12, 0.5, 0.12, new Particle.DustOptions(Color.SILVER, 2));
        }
        owner.setAllowFlight(true);
        owner.setInvulnerable(true);
        owner.sendMessage(ChatColor.GREEN + "Engaging jetpack!");
        bossBar.setTitle(ChatColor.GREEN + "Jetpack Engaged!");
        owner.getInventory().setItem(plugin.getConfig().getInt("EngageItemslot", 8), plugin.getCooldownItem());
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!outOfFuel && owner.getInventory().getItem(plugin.getConfig().getInt("EngageItemslot", 8)).isSimilar(plugin.getCooldownItem()))
                owner.getInventory().setItem(plugin.getConfig().getInt("EngageItemslot", 8), plugin.getDisengageItem());
        }, 30);    }

    public void disengage() {
        engaged = false;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> owner.setInvulnerable(false), 100);
        owner.sendMessage(ChatColor.RED + "Disengaging jetpack!");
        bossBar.setTitle(ChatColor.RED + "Jetpack Disengaged!");
        fuelingCooldown = true;
        owner.setAllowFlight(false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> fuelingCooldown = false, 40);
        owner.getInventory().setItem(plugin.getConfig().getInt("EngageItemslot", 8), plugin.getCooldownItem());
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!outOfFuel && owner.getInventory().getItem(plugin.getConfig().getInt("EngageItemslot", 8)).isSimilar(plugin.getCooldownItem()))
                owner.getInventory().setItem(plugin.getConfig().getInt("EngageItemslot", 8), plugin.getEngageItem());
        }, 30);
    }

    public void outOfFuel() {
        owner.sendMessage(ChatColor.RED + "Jetpack out of fuel!");
        bossBar.setProgress(0.0);
        bossBar.setTitle(ChatColor.RED + "Jetpack out of fuel!");
        owner.setAllowFlight(false);
        fuelingCooldown = true;
        engaged = false;
        outOfFuel = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            bossBar.setTitle(ChatColor.DARK_RED + "Jetpack out of fuel!");
            owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }, 4);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            bossBar.setTitle(ChatColor.RED + "Jetpack out of fuel!");
            owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.5f);
        }, 8);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            bossBar.setTitle(ChatColor.DARK_RED + "Jetpack out of fuel!");
            owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }, 12);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            bossBar.setTitle(ChatColor.RED + "Jetpack out of fuel!");
            owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.5f);
        }, 16);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            bossBar.setTitle(ChatColor.DARK_RED + "Jetpack out of fuel!");
            owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }, 20);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            bossBar.setTitle(ChatColor.RED + "Jetpack out of fuel!");
            owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.5f);
        }, 24);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> fuelingCooldown = false, 40);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> owner.setInvulnerable(false), 200);
        owner.getInventory().setItem(plugin.getConfig().getInt("EngageItemslot", 8), plugin.getOutOfFuelItem());
    }
}
