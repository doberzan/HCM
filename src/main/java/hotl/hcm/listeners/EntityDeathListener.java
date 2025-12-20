package hotl.hcm.listeners;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import hotl.hcm.runnables.DragonsRevengeFight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StructureSearchResult;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;
import hotl.hcm.HCMPlayer;

public class EntityDeathListener implements Listener {
	private HCM plugin;
	private HCMGame game;
	public EntityDeathListener(HCM plugin) {
		this.plugin = plugin;
		this.game = plugin.game;
	}
	
	public void postGameMobSpawn() 
	{
		Location loc = new Location(Bukkit.getWorld("world_the_end"), 5,68,0);
		new DragonsRevengeFight(loc, plugin).runTaskTimer(plugin, 20*40L, 20L * 10);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity mob = event.getEntity();

		// Do nothing if game has not started or is finished
		if(!plugin.game.isGameRunning() || plugin.game.isGameFinished())
		{
			return;
		}

		if (mob.getKiller() != null && mob.getKiller().getType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity().getKiller();
			HCMPlayer hcmPlayer = plugin.game.HCMPlayers.get(player.getUniqueId());

			// Player killed enderman
			if (mob.getType().equals(EntityType.ENDERMAN))
			{
				hcmPlayer.setEndermanKilled(hcmPlayer.getEndermanKilled()+1);
			}

			// Player killed blaze
			if (mob.getType().equals(EntityType.BLAZE))
			{
				hcmPlayer.setBlazeKilled(hcmPlayer.getBlazeKilled()+1);
			}

			// Elder Guardian has been killed by a player
			if (mob.getType() == EntityType.ELDER_GUARDIAN) {
				
				game.setGuardianIsDead(true);
				game.HCMPlayers.get(player.getUniqueId()).setKilledGuardian(true);
				
				ItemStack brains = new ItemStack(Material.BRAIN_CORAL, 1);
				ItemMeta im = (ItemMeta) brains.getItemMeta();
				im.setDisplayName(ChatColor.GREEN + "Elder Guardian Brains");
				im.setLore(Arrays.asList(ChatColor.YELLOW + "Use this on an end portal frame to enable the end."));
				brains.setItemMeta(im);
				if (player.getInventory().firstEmpty() == -1) {
					player.getWorld().dropItemNaturally(player.getLocation(), brains);
				} else {
					player.getInventory().setItem(player.getInventory().firstEmpty(), brains);
				}

				for (Player p : Bukkit.getOnlinePlayers()) {
					p.getWorld().playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
					p.sendMessage(HCM.formatHCM(ChatColor.LIGHT_PURPLE + player.getDisplayName() + ChatColor.YELLOW
							+ " has defeated the Elder Guardian!"));
				}
			}


			// Enderdragon is killed by player
			if (mob.getType() == EntityType.ENDER_DRAGON && game.isGameRunning() && !game.isGameFinished()) {
				game.setGameFinished(true);
				game.setGameRunning(false);
				game.setGameEndTime(LocalDateTime.now());
				game.setEnderDragonIsDead(true);
				game.HCMPlayers.get(player.getUniqueId()).setKilledEnderDragon(true);
				
				Duration duration = Duration.between(game.getGameStartTime(), game.getGameEndTime());
				DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(HCM.formatHCM(ChatColor.LIGHT_PURPLE + player.getDisplayName() + ChatColor.YELLOW
							+ " has defeated the Ender Dragon!"));
					p.sendMessage(HCM.formatHCM("Total game time was: " + ChatColor.AQUA + String.format("%d:%02d:%02d", duration.toHours(), duration.toMinutes() % 60, duration.getSeconds() % 60)));
				}
				postGameMobSpawn();
			}
			
			if (mob.getType() == EntityType.END_CRYSTAL) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
				}
			}

				
			game.setMobsKilled(game.getMobsKilled() + 1);
			HCMPlayer ps = game.HCMPlayers.get(player.getUniqueId());
			ps.setMobKills(ps.getMobKills()+1);

			if (game.getMobsKilled() == plugin.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS) {
				for (Player p : Bukkit.getOnlinePlayers()) {

					World w = p.getWorld();
					w.playSound(p.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_5, 0.5f, 1);
					StructureSearchResult loc = Bukkit.getServer().getWorld("world_nether").locateNearestStructure(
							new Location(Bukkit.getServer().getWorld("world_nether"), 0, 0, 0), Structure.FORTRESS,
							1000, false);
					p.sendMessage(HCM.formatHCM(
							ChatColor.GOLD + "Murder spree over! The closest nether fortress coordinates are "
									+ ChatColor.BOLD + ChatColor.GREEN + "" + loc.getLocation().getBlockX() + ", " + loc.getLocation().getBlockZ() + ChatColor.GOLD + "!"));
				}

			} else if (game.getMobsKilled() % (plugin.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS / 6) == 0 && game.getMobsKilled() < plugin.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					World w = p.getWorld();
					w.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					p.sendMessage(HCM.formatHCM(
							ChatColor.LIGHT_PURPLE + "" + mob.getKiller().getName() + ChatColor.GREEN + " killed " + ChatColor.GOLD + "" + mob.getType().name() + ChatColor.GREEN + " for a total of "+ ChatColor.AQUA + "" +  game.getMobsKilled() + ChatColor.GREEN + " out of " + ChatColor.AQUA
									+ plugin.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS + ChatColor.GREEN + " mobs killed!"));
				}
			}
		}
	}
}

