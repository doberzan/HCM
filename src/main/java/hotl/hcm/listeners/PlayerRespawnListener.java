package hotl.hcm.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;

public class PlayerRespawnListener implements Listener {
	private HCM plugin;
	private HCMGame game;
	
	public PlayerRespawnListener(HCM plugin) {
		this.plugin = plugin;
		this.game = plugin.game;
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player deadplayer = event.getPlayer();
		
		if (game.HCMPlayers.get(deadplayer.getUniqueId()).getPlayerMode() == 2) {
			deadplayer.setGameMode(GameMode.SPECTATOR);
		}
		
		if (game.HCMPlayers.get(deadplayer.getUniqueId()).getPlayerMode() == 1) {
			deadplayer.setGameMode(GameMode.ADVENTURE);
			deadplayer.sendMessage(HCM.formatHCM(
					"You have lost your ability to do most things except for fight. This is your last chance don't mess it up!"));
			final String playername = deadplayer.getDisplayName();
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					Bukkit.getPlayer(playername).setGameMode(GameMode.ADVENTURE);
				}
			}, 1L);
			
			ItemStack item = new ItemStack(Material.SUNFLOWER, 2);
			ItemMeta meta = item.getItemMeta();
			LeatherArmorMeta armorMeta;
            
            // Create armor
            
            // helmet
            item = new ItemStack(Material.LEATHER_HELMET);
            armorMeta = (LeatherArmorMeta) item.getItemMeta();
            
            armorMeta.setColor(Color.RED);
            armorMeta.setDisplayName(ChatColor.GOLD + "Guardian's Helm");
            armorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Bound to protect..."));
            armorMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            item.setItemMeta(armorMeta);
            deadplayer.getInventory().setHelmet(item);
            
            // chestplate
            item = new ItemStack(Material.LEATHER_CHESTPLATE);
            armorMeta = (LeatherArmorMeta) item.getItemMeta();
            
            armorMeta.setColor(Color.RED);
            armorMeta.setDisplayName(ChatColor.GOLD + "Guardian's Chestplate");
            armorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Bound to protect..."));
            armorMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            armorMeta.addEnchant(Enchantment.UNBREAKING, 5, true);
            item.setItemMeta(armorMeta);
            deadplayer.getInventory().setChestplate(item);
            
            // leggings
            item = new ItemStack(Material.LEATHER_LEGGINGS);
            armorMeta = (LeatherArmorMeta) item.getItemMeta();
            
            armorMeta.setColor(Color.RED);
            armorMeta.setDisplayName(ChatColor.GOLD + "Guardian's Leggings");
            armorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Bound to protect..."));
            armorMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            armorMeta.addEnchant(Enchantment.UNBREAKING, 5, true);
            item.setItemMeta(armorMeta);
            deadplayer.getInventory().setLeggings(item);
            
            // leggings
            item = new ItemStack(Material.GOLDEN_BOOTS);
            meta = item.getItemMeta();
            
            meta.setDisplayName(ChatColor.GOLD + "Guardian's Boots");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Bound to protect...", ChatColor.GRAY + "- Dissuades the pigs."));
            meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            meta.addEnchant(Enchantment.UNBREAKING, 5, true);
            item.setItemMeta(meta);
            deadplayer.getInventory().setBoots(item);
            
            // sword
            item = new ItemStack(Material.IRON_SWORD);
            meta = item.getItemMeta();
            
            meta.setDisplayName(ChatColor.GREEN + "Guardian's Blade");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Built for precision cutting."));
            meta.addEnchant(Enchantment.UNBREAKING, 5, true);
            item.setItemMeta(meta);
            deadplayer.getInventory().addItem(item);
            
            // Create tp token
            item = new ItemStack(Material.SUNFLOWER, 2);
            meta.setDisplayName(ChatColor.GOLD + "Token of Chance");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Grants the user the ability", ChatColor.GRAY + "to warp to a survivor."));
            item.setItemMeta(meta);
            deadplayer.getInventory().addItem(item);
	        
		}

	}

}
