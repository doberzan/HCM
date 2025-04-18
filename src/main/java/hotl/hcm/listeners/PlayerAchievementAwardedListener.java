package hotl.hcm.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;

public class PlayerAchievementAwardedListener implements Listener {
	private HCM plugin;
	private HCMGame game;
	public PlayerAchievementAwardedListener(HCM plugin) {
		this.plugin = plugin;
		this.game = plugin.game;
	}

	@EventHandler
	public void onPlayerAchievement(PlayerAdvancementDoneEvent event) {

		if (event.getAdvancement().getKey().getKey().equals("nether/find_bastion")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				World w = p.getWorld();
				w.playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 100, 1);
				p.sendMessage(HCM.formatHCM(ChatColor.LIGHT_PURPLE + event.getPlayer().getName() + ChatColor.GOLD
						+ " has decided they want to die! Obviously this is the only reason you would enter a bastion..."));
			}
			event.getPlayer()
					.sendMessage(HCM.formatHCM(ChatColor.RED + "" + ChatColor.UNDERLINE + "" + "You are an idiot :)"));
			event.getPlayer()
					.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, 1, true, true, true));
			event.getPlayer().addPotionEffect(
					new PotionEffect(PotionEffectType.BAD_OMEN, Integer.MAX_VALUE, 1, false, false, true));
			game.HCMPlayers.get(event.getPlayer().getUniqueId()).setEnteredBastion(true);;
		}
	}

}
