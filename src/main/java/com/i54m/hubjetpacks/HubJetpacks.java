package com.i54m.hubjetpacks;

import com.i54m.hubjetpacks.listeners.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class HubJetpacks extends JavaPlugin {

    @Getter
    @Setter
    public static HubJetpacks instance;
    @Getter
    private ItemStack jetpack;
    @Getter
    private final ItemStack engageItem = new ItemStack(Material.LIME_DYE, 1);
    @Getter
    private final ItemStack outOfFuelItem = new ItemStack(Material.GRAY_DYE, 1);
    @Getter
    private final ItemStack cooldownItem = new ItemStack(Material.GRAY_DYE, 1);
    @Getter
    private final ItemStack disengageItem = new ItemStack(Material.RED_DYE, 1);
    @Getter
    private final ItemStack bouncyBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
    @Getter
    private final ItemStack spaceSuitLegs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
    @Getter
    private HashMap<Player, Jetpack> jetpacks = new HashMap<>();
    private int jetpackTaskId;


    @Override
    public void onEnable() {
        setInstance(this);
        if (!new File(getDataFolder(), "config.yml").exists())
            saveDefaultConfig();
        reloadConfig();
        jetpack = new ItemStack(Material.valueOf(getConfig().getString("JetpackItem.material", "IRON_CHESTPLATE")), 1);
        ItemMeta im = jetpack.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString("JetpackItem.name")));
        List<String> lore = new ArrayList<>();
        getConfig().getStringList("JetpackItem.lore").forEach((i) -> {
            lore.add(ChatColor.translateAlternateColorCodes('&', i));
        });
        im.setLore(lore);
        jetpack.setItemMeta(im);
        im = engageItem.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Engage jetpack" + ChatColor.GRAY + " (Right Click)");
        im.setLore(Arrays.asList(ChatColor.GREEN + "Jet pack is not currently engaged!",
                ChatColor.WHITE + "Right click this item to engage the jetpack and",
                ChatColor.WHITE + "fly the direction you are looking!"));
        engageItem.setItemMeta(im);
        im = disengageItem.getItemMeta();
        im.setDisplayName(ChatColor.RED + "Disengage jetpack" + ChatColor.GRAY + " (Right Click)");
        im.setLore(Arrays.asList(ChatColor.RED + "Jet pack is currently engaged!",
                ChatColor.WHITE + "Right click this item to disengage the jetpack",
                ChatColor.WHITE + "and let gravity take you on a ride!"));
        disengageItem.setItemMeta(im);
        im = outOfFuelItem.getItemMeta();
        im.setDisplayName(ChatColor.GRAY + "Jetpack out of fuel!");
        im.setLore(Arrays.asList(ChatColor.RED + "Jet pack is currently out of fuel!",
                ChatColor.WHITE + "You must wait for your jetpack to regain",
                ChatColor.WHITE + "some fuel before you use it again!"));
        outOfFuelItem.setItemMeta(im);
        im = cooldownItem.getItemMeta();
        im.setDisplayName(ChatColor.GRAY + "Jetpack toggling on cooldown!");
        im.setLore(Arrays.asList(ChatColor.RED + "Jet pack toggling is currently on cooldown!",
                ChatColor.WHITE + "You must wait for your jetpack to cooldown",
                ChatColor.WHITE + "enough before you toggle it again!"));
        cooldownItem.setItemMeta(im);
        LeatherArmorMeta lam = (LeatherArmorMeta) bouncyBoots.getItemMeta();
        lam.setColor(Color.WHITE);
        lam.setDisplayName(ChatColor.WHITE + "BouncyBoots");
        lam.setLore(Arrays.asList(ChatColor.WHITE + "These boots are cushioned just right",
                ChatColor.WHITE + "so that you take no fall damage!"));
        bouncyBoots.setItemMeta(lam);
        lam = (LeatherArmorMeta) spaceSuitLegs.getItemMeta();
        lam.setColor(Color.WHITE);
        lam.setDisplayName(ChatColor.WHITE + "Space Suit Leggings");
        lam.setLore(Arrays.asList(ChatColor.WHITE + "These leggings help make sure you don't",
                ChatColor.WHITE + "freeze to death in space!"));
        spaceSuitLegs.setItemMeta(lam);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerKickListener(), this);
        jetpackTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : jetpacks.keySet()) {
                if (player.isOnline()) {
                    Jetpack jetpack = jetpacks.get(player);
                    BossBar bossBar = jetpack.getBossBar();
                    if (jetpack.isEngaged()) {
                        player.setFlying(false);
                        if (player.getLocation().subtract(0, 3, 0).getBlock().getType().isSolid() ||
                                player.getLocation().subtract(0, 2, 0).getBlock().getType().isSolid() ||
                                player.getLocation().subtract(0, 1, 0).getBlock().getType().isSolid()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, .5f, 1);
                            player.setVelocity(player.getLocation().getDirection().add(new Vector(0, 4, 0)));
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, .5f, 0.25f);
                            player.setVelocity(player.getLocation().getDirection().normalize().multiply(1.5D));
                        }
                        if (!(bossBar.getProgress() - 0.01 <= 0)) bossBar.setProgress(bossBar.getProgress() - 0.01);
                        else bossBar.setProgress(0.0);
                        player.spawnParticle(Particle.REDSTONE, player.getLocation().add(0, 0.75, 0), 5, 0.12, 0.25, 0.12, new Particle.DustOptions(Color.fromRGB(250,100, 0), 1));
                        player.spawnParticle(Particle.REDSTONE, player.getLocation().add(0, -0.5, 0), 15, 0.12, 0.5, 0.12, new Particle.DustOptions(Color.SILVER, 2));
                        if (bossBar.getProgress() <= 0) jetpack.outOfFuel();
                    } else if (jetpack.isOutOfFuel() && !jetpack.isFuelingCooldown()) {
                        if (jetpack.getBossBar().getProgress() + 0.01 <= 1.0)
                            jetpack.getBossBar().setProgress(jetpack.getBossBar().getProgress() + 0.02);
                        jetpack.getBossBar().setTitle(ChatColor.GOLD + "Refueling jetpack...");
                        if (jetpack.getBossBar().getProgress() >= 0.1) {
                            jetpack.setOutOfFuel(false);
                            player.sendMessage(ChatColor.GREEN + "Jetpack 10% refueled, you may use it again!");
                            player.getInventory().setItem(getConfig().getInt("EngageItemslot", 8), engageItem);
                        }
                    } else if ((jetpack.getBossBar().getProgress() + 0.01 <= 1.0) && !jetpack.isFuelingCooldown()) {
                        jetpack.getBossBar().setTitle(ChatColor.GOLD + "Refueling jetpack...");
                        jetpack.getBossBar().setProgress(jetpack.getBossBar().getProgress() + 0.01);
                    } else if (!jetpack.isFuelingCooldown())
                        jetpack.getBossBar().setTitle(ChatColor.GREEN + "Jetpack ready!");
                } else jetpacks.remove(player);
            }
        }, 20, 2);
    }

    @Override
    public void onDisable() {
        jetpacks = new HashMap<>();
        Bukkit.getScheduler().cancelTask(jetpackTaskId);
    }

    public Jetpack getJetpack(Player player) {
        return jetpacks.get(player);
    }
}
