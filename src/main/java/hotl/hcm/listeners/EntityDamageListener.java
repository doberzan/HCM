package hotl.hcm.listeners;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StructureSearchResult;
import org.bukkit.util.Vector;

import hotl.hcm.HCM;

public class EntityDamageListener implements Listener {
	
	HCM plugin;
	Random rand = new Random();
	public EntityDamageListener(Plugin p) {
		plugin = (HCM) p;
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		Entity mob = event.getEntity();
		
		if (mob.getType() == EntityType.ENDER_DRAGON && rand.nextInt(8) == 5 && mob.getLocation().getY() > 80) 
		{
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getWorld() == mob.getWorld()) {
						Location mobloc = new Location(mob.getWorld(), mob.getLocation().getX(),mob.getLocation().getY()-3, mob.getLocation().getZ());
						Vector direction = player.getLocation().toVector().clone().subtract(mobloc.toVector().clone());
						Fireball fireball1 = player.getWorld().spawn(mobloc, Fireball.class);
						
		                fireball1.setIsIncendiary(true);
		                fireball1.setVelocity(direction.normalize().multiply(1.3));
		                fireball1.setDirection(direction.normalize());
				}
			}
		}
	}
	
}
