package hotl.hcm.listeners;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.Plugin;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;

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
        // Check if the player entered the End world
        if (world.getEnvironment() == World.Environment.THE_END && !game.HCMPlayers.get(player.getUniqueId()).isEnteredEnd()) {
        	game.HCMPlayers.get(player.getUniqueId()).setEnteredEnd(true);
        	player.playSound(new Location(player.getWorld(), 0,150,0), Sound.MUSIC_DISC_PIGSTEP, 0.7f, 1.0f);
        }
        if (world.getEnvironment() == World.Environment.NETHER && !game.HCMPlayers.get(player.getUniqueId()).isEnteredNether()) {
        	game.HCMPlayers.get(player.getUniqueId()).setEnteredNether(true);
        	player.playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1f, 1.0f);
        }
    }
}
