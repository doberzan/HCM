package hotl.hcm.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;

public class PlayerMoveListener implements Listener{
	private HCM plugin;
	private HCMGame game;
	
	public PlayerMoveListener(HCM plugin) 
	{
		this.plugin = plugin;
		this.game = plugin.game;
	}
	
	@EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
		if(!game.isGameRunning() && !event.getPlayer().isOp()) 
		{
			event.setCancelled(true);
		}
	}
}
