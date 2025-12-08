package hotl.hcm.listeners;

import hotl.hcm.HCMPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.Plugin;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerChangeWorldListener implements Listener {
	private HCM plugin;
	private HCMGame game;
	public PlayerChangeWorldListener(HCM plugin) {
		this.plugin = plugin;
		this.game = plugin.game;
	}
	
	@EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

		// Do nothing if game has not started
		if(!plugin.game.isGameRunning())
		{
			return;
		}

        // Check if the player entered the End world
        if (world.getEnvironment() == World.Environment.THE_END && !game.HCMPlayers.get(player.getUniqueId()).isEnteredEnd()) {
			boolean firstPlayerToEnterEnd = true;

			// Jank way to check first player entered end
			for (HCMPlayer p : game.HCMPlayers.values())
			{
				if(p.isEnteredEnd())
				{
					firstPlayerToEnterEnd = false;
					break;
				}
			}

			if(firstPlayerToEnterEnd) {
				firstPlayerEnteredEnd(player);
			}

        	game.HCMPlayers.get(player.getUniqueId()).setEnteredEnd(true);
			return;
        }

        // Check if the player entered the Nether world
		if (world.getEnvironment() == World.Environment.NETHER && !game.HCMPlayers.get(player.getUniqueId()).isEnteredNether()) {
			boolean firstPlayerToEnterNether = true;
			// Jank way to check first player entered nether
			for (HCMPlayer p : game.HCMPlayers.values())
			{
				if(p.isEnteredNether())
				{
					firstPlayerToEnterNether = false;
					break;
				}
			}
			if(firstPlayerToEnterNether) {
				firstPlayerEnteredNether(player);
			}
			game.HCMPlayers.get(player.getUniqueId()).setEnteredNether(true);
        }
    }

	private void firstPlayerEnteredEnd(Player player) {

		player.playSound(player.getLocation().add(0,30,0), Sound.MUSIC_DISC_PIGSTEP, 20f, 1.0f);

		Duration duration = Duration.between(game.getGameStartTime(), LocalDateTime.now());
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		for (Player pl : Bukkit.getOnlinePlayers())
		{
			player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1f, 1.0f);
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.0f);
			pl.sendMessage(HCM.formatHCM(ChatColor.AQUA + "" + player.getDisplayName() + ChatColor.GOLD + " was the first player to enter the End!"));
			pl.sendMessage(HCM.formatHCM("End entry time is " + ChatColor.GREEN + "" + String.format("%d:%02d:%02d", duration.toHours(), duration.toMinutes() % 60, duration.getSeconds() % 60)));
		}
	}

	private void firstPlayerEnteredNether(Player player) {
		Duration duration = Duration.between(game.getGameStartTime(), LocalDateTime.now());
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		for (Player pl : Bukkit.getOnlinePlayers())
		{
			player.playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1f, 1.0f);
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.0f);
			pl.sendMessage(HCM.formatHCM(ChatColor.AQUA + "" + player.getDisplayName() + ChatColor.GOLD + " was the first player to enter the Nether!"));
			pl.sendMessage(HCM.formatHCM("Nether entry time is " + ChatColor.GREEN + "" + String.format("%d:%02d:%02d", duration.toHours(), duration.toMinutes() % 60, duration.getSeconds() % 60)));
		}
	}
}
