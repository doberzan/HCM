package hotl.hcm.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import hotl.hcm.HCM;

public class PlayerChatListener implements Listener{
	private HCM plugin;

	public PlayerChatListener(HCM plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
	    Player player = event.getPlayer();
	    String message = event.getMessage();

	    event.setFormat(player.getDisplayName() + ChatColor.GREEN + "" + ChatColor.BOLD +  " > " + ChatColor.RESET + message);
	}
}
