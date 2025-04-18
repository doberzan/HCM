package hotl.hcm.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;

public class PlayerDeathListener implements Listener {
	private List<Player> playerList;
	private HCM plugin;
	private HCMGame game;

	public PlayerDeathListener(HCM plugin) {
		this.plugin = plugin;
		this.game = plugin.game;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player deadplayer = (Player) event.getEntity();

		// Players first death
		if (game.HCMPlayers.get(deadplayer.getUniqueId()).getPlayerMode() == 0) {
			
			game.HCMPlayers.get(deadplayer.getUniqueId()).setPlayerMode(1);
			if (deadplayer.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
	            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) deadplayer.getLastDamageCause();
	            Entity killer = damageEvent.getDamager();

	            // Check if the killer is a LivingEntity (e.g., player, mob, etc.)
	            if (killer instanceof LivingEntity) {
	                LivingEntity livingKiller = (LivingEntity) killer;
	                game.HCMPlayers.get(deadplayer.getUniqueId()).setCauseOfDeath(livingKiller+ ":" + deadplayer.getLastDamageCause().getCause().toString());
	            }else 
	            {
		        	game.HCMPlayers.get(deadplayer.getUniqueId()).setCauseOfDeath(event.getDeathMessage()+ ":" + deadplayer.getLastDamageCause().getCause().toString());
	            }
	        }else 
	        {
	        	game.HCMPlayers.get(deadplayer.getUniqueId()).setCauseOfDeath(event.getDeathMessage()+ ":" + deadplayer.getLastDamageCause().getCause().toString());
	        }
			
			playerList = new ArrayList<Player>(Bukkit.getOnlinePlayers());
			for (Player p : playerList) {
				World w = p.getWorld();
				w.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100, 1);
				w.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 50, 1);
				p.sendTitle(ChatColor.LIGHT_PURPLE + deadplayer.getName(), ChatColor.GREEN + "is bad at minecraft", 30,
						200, 30);
				p.sendMessage(HCM.formatHCM(ChatColor.LIGHT_PURPLE + deadplayer.getName() + ChatColor.GOLD
						+ " has lost their MINING privileges!"));
				p.sendMessage(HCM.formatHCM(ChatColor.GOLD + "They have been assigned to" + ChatColor.RED
						+ " protection " + ChatColor.GOLD + "duty!"));
				deadplayer.getWorld().strikeLightningEffect(deadplayer.getLocation());
				deadplayer.getWorld().strikeLightningEffect(deadplayer.getLocation());
			}
		}else 
		{
			game.HCMPlayers.get(deadplayer.getUniqueId()).setPlayerMode(2);
			// Players second death
			// Drop potato
			ItemStack potato = new ItemStack(Material.POTATO, 1);
			ItemMeta im = (ItemMeta) potato.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + deadplayer.getDisplayName() + "'s corpse");
			im.setLore(
					Arrays.asList(ChatColor.YELLOW + "They were so bad at minecraft they turned into a potato."));
			potato.setItemMeta(im);
			World w1 = deadplayer.getWorld();
			w1.dropItemNaturally(deadplayer.getLocation(), potato);

			playerList = new ArrayList<Player>(Bukkit.getOnlinePlayers());
			for (Player p : playerList) {
				World w2 = p.getWorld();
				w2.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100, 1);
				w2.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 50, 1);
				p.sendTitle(ChatColor.LIGHT_PURPLE + deadplayer.getName(),
						ChatColor.RED + "has lost living priveleges", 30, 200, 30);
				p.sendMessage(HCM.formatHCM(
						ChatColor.LIGHT_PURPLE + deadplayer.getName() + ChatColor.GOLD + " is now a potato."));
			}
			deadplayer.getWorld().strikeLightningEffect(deadplayer.getLocation());
			deadplayer.getWorld().strikeLightningEffect(deadplayer.getLocation());
						
		}
	}

}
