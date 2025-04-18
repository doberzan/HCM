package hotl.hcm.listeners;


import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import hotl.hcm.HCM;
import hotl.hcm.HCMGame;

public class PlayerDamageListener implements Listener {
	private HCM plugin;
	private HCMGame game;
	public PlayerDamageListener(HCM plugin) {
		this.plugin = plugin;
		this.game = plugin.game;
	}


	@EventHandler
	public void PlayerDamageEvent(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			if(!game.isGameRunning())
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void PlayerHealEvent(EntityRegainHealthEvent event) {
		
	}
}
