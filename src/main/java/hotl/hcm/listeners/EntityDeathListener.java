package hotl.hcm.listeners;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
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
		Location loc = new Location(Bukkit.getWorld("world_the_end"), 5,70,0);
		new SilverfishSpawnerTask(loc).runTaskTimer(plugin, 0L, 20L * 10);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity mob = event.getEntity();
		
		if (mob.getKiller() != null && mob.getKiller().getType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity().getKiller();
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
			
			if (mob.getType() == EntityType.ENDER_DRAGON && game.isGameRunning() && !game.isGameFinished()) {
				
				game.setGameRunning(false);
				game.setGameEndTime(LocalDateTime.now());
				game.setEnderDragonIsDead(true);
				game.HCMPlayers.get(player.getUniqueId()).setKilledEnderDragon(true);
				
				Duration duration = Duration.between(game.getGameStartTime(), game.getGameEndTime());
				DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(HCM.formatHCM(ChatColor.LIGHT_PURPLE + player.getDisplayName() + ChatColor.YELLOW
							+ " has defeated the Ender Dragon!"));
					p.sendMessage(HCM.formatHCM("Total game time was: " + ChatColor.AQUA + duration.toHours() + ":" + duration.toMinutes() % 60 + ":" + duration.getSeconds() % 60));
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

			if (game.getMobsKilled() == HCM.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS) {
				for (Player p : Bukkit.getOnlinePlayers()) {

					World w = p.getWorld();
					w.playSound(p.getLocation(), Sound.ENTITY_WOLF_HOWL, 0.5f, 1);
					StructureSearchResult loc = Bukkit.getServer().getWorld("world_nether").locateNearestStructure(
							new Location(Bukkit.getServer().getWorld("world_nether"), 0, 0, 0), Structure.FORTRESS,
							1000, false);
					p.sendMessage(HCM.formatHCM(
							ChatColor.GOLD + "Murder spree over! The closest nether fortress coordinates are "
									+ ChatColor.BOLD + ChatColor.GREEN + "" + loc.getLocation().getBlockX() + ", " + loc.getLocation().getBlockZ() + ChatColor.GOLD + "!"));
				}

			} else if (game.getMobsKilled() % (HCM.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS / 6) == 0 && game.getMobsKilled() < HCM.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					World w = p.getWorld();
					w.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					p.sendMessage(HCM.formatHCM(
							ChatColor.LIGHT_PURPLE + "" + mob.getKiller().getName() + ChatColor.GREEN + " killed " + ChatColor.GOLD + "" + mob.getType().name() + ChatColor.GREEN + " for a total of "+ ChatColor.AQUA + "" +  game.getMobsKilled() + ChatColor.GREEN + " out of " + ChatColor.AQUA
									+ HCM.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS + ChatColor.GREEN + " mobs killed!"));
				}
			}
		}
	}
}

class SilverfishSpawnerTask extends BukkitRunnable {
	private final Location location;
	private int waves;
	private int currWave;
    public SilverfishSpawnerTask(Location location) {
        this.location = location;
        this.waves = 10;
        this.currWave = 0;
    }
	public void run() {
		PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 3);
		if(currWave > waves) 
		{
			this.cancel();
		}
		currWave ++;
		for(int i = 0; i < 30; i ++) 
		{
			Silverfish silverfish = (Silverfish) location.getWorld().spawnEntity(location, EntityType.SILVERFISH);
            silverfish.setCustomName(ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "Dragon's Revenge");
            silverfish.addPotionEffect(speedEffect);
		}
		
	}
	
}
