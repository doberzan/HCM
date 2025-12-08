package hotl.hcm.listeners;

import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;
import hotl.hcm.HCMPlayer;
import net.md_5.bungee.api.ChatColor;

public class PlayerJoinListener implements Listener{
	private HCM plugin;
	private HCMGame game;
	
	public PlayerJoinListener(HCM plugin) 
	{
		this.plugin = plugin;
		this.game = plugin.game;
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event) 
	{
		if(game.HCMPlayers.keySet().contains(event.getPlayer().getUniqueId())) 
		{
			// Replace old player object for rejoined player
			game.HCMPlayers.get(event.getPlayer().getUniqueId()).setPlayer(event.getPlayer());
		}else 
		{
			game.HCMPlayers.put(event.getPlayer().getUniqueId(), new HCMPlayer(event.getPlayer()));
		}
		event.getPlayer().setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + event.getPlayer().getName()+ ChatColor.RESET);
	}
}
