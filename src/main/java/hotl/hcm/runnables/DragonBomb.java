package hotl.hcm.runnables;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import hotl.hcm.HCM;

public class DragonBomb implements Runnable {
	private HCM plugin;
	
	public DragonBomb(HCM plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		// Make a list of active ender dragons
		for (Entity entity : Bukkit.getWorld("world_the_end").getEntities()) {
			if (entity instanceof EnderDragon && entity.getLocation().getChunk().isLoaded()
					&& getDistanceToGround(entity) > 10) {
				for (int i = 0; i < 100; i++) {
					Bukkit.getScheduler().runTaskLater(plugin, () -> {
						TNTPrimed tnt = entity.getWorld().spawn(entity.getLocation(), TNTPrimed.class, primed -> {
							primed.setFuseTicks(5 * 20);
							primed.setYield(5);
							primed.setIsIncendiary(true);
							primed.setSource(entity);
						});
						Random random = new Random();
						Vector baseVelocity = entity.getVelocity();

						double offsetX = (random.nextDouble() * 2) - 2;
						double offsetY = 2;
						double offsetZ = (random.nextDouble() * 2) - 2;

						Vector randomVelocity = baseVelocity.clone().add(new Vector(offsetX, offsetY, offsetZ));

						tnt.setVelocity(randomVelocity);
						entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
				    }, i*(i%2));
				}
				//for(int i =0; i < 10; i ++)
				//{
				//	Ghast ghast = (Ghast) entity.getWorld().spawn(new Location(entity.getWorld(), 0,80,0), Ghast.class);
				//}
			}
		}
	}

	public double getDistanceToGround(Entity entity) {
		Location loc = entity.getLocation();
		World world = loc.getWorld();
		int startY = loc.getBlockY();

		for (int y = startY; y >= 0; y--) {
			Material blockType = world.getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).getType();
			if (blockType.isSolid()) {
				return loc.getY() - (y + 1);
			}
		}
		return Double.MAX_VALUE;
	}

}
