package hotl.hcm.listeners;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;
import hotl.hcm.HCMPlayer;

public class PlayerInteractListener implements Listener {
	private HCM plugin;
	private HCMGame game;
	
	public PlayerInteractListener(HCM plugin) {
		this.plugin = plugin;
		this.game = plugin.game;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!game.isGameRunning() && !event.getPlayer().isOp() && !game.isGameFinished())
		{
			event.setCancelled(true);
			return;
		}
		if (event.getItem() != null) {
			if (event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Token of Chance")) {
				if (game.HCMPlayers.get(event.getPlayer().getUniqueId()).getPlayerMode() == 1) {
					ArrayList<Player> alivePlayers = new ArrayList<Player>();
					for (HCMPlayer p : game.HCMPlayers.values()) {
						if (p.getPlayerMode() == 0) {
							alivePlayers.add(p.getPlayer());
						}
					}
					if (alivePlayers.size() > 0) {
						Random r = new Random();
						event.getPlayer().teleport(alivePlayers.get(r.nextInt(alivePlayers.size())));

						if (event.getItem() != null && event.getItem().getAmount() > 0) {
							// Decrease the amount by 1
							event.getItem().setAmount(event.getItem().getAmount() - 1);

							// If the amount is now 0, remove the item from the inventory
							if (event.getItem().getAmount() == 0) {
								event.getPlayer().getInventory().setItemInMainHand(null);
							}

							event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,
									1, 1);
						}
					}

				} else {
					event.getPlayer().sendMessage(HCM.formatHCM("You are not worthy of the token..."));
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, (float) 0.5,
							1);
				}
			}
		}
	}

}
