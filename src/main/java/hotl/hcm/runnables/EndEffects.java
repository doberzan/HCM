package hotl.hcm.runnables;

import hotl.hcm.HCM;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class EndEffects implements Runnable {
    private HCM hcm;

    public EndEffects(HCM hcm) {
        this.hcm = hcm;
    }


    @Override
    public void run() {
        // Check if there are players in the end
        World end = null;
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (p.getWorld().getEnvironment() == World.Environment.THE_END)
            {
                end = p.getWorld();
                break;
            }
        }
        if (end == null)
        {
            return;
        }
        // Strike pillars
        strikePillars(end);

    }

    private void strikePillars(World end) {
        ArrayList<Location> pillars = new ArrayList<>();
        Location start = new Location(end, 0, 60, 0);
        Collection<Entity> found = end.getNearbyEntities(start, 100, 150, 100);
        long i = 0;
        for (Entity e : found)
        {
            if (e.getType() != EntityType.END_CRYSTAL)
            {
                continue;
            }
            Bukkit.getScheduler().runTaskLater(hcm, () -> {
                end.strikeLightningEffect(e.getLocation().clone());
            }, i);
            i += 10;
        }
    }
}
