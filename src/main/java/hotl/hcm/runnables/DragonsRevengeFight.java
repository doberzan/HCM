package hotl.hcm.runnables;

import hotl.hcm.HCM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class DragonsRevengeFight extends BukkitRunnable {
    private final Location location;
    private int waves;
    private int currWave;

    public DragonsRevengeFight(Location location, HCM hcm) {
        this.location = location;
        this.waves = 10;
        this.currWave = 0;
        Bukkit.getScheduler().runTaskLater(hcm, () -> {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                p.teleport(location);
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage(HCM.formatHCM("Something isn't right..."));
                p.sendMessage(HCM.formatHCM(ChatColor.RED + "" + ChatColor.BOLD + "You should run"));
            }
        }, 20*30);
    }
    public void run() {
        PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 3);
        if(currWave > waves)
        {
            this.cancel();
        }
        currWave ++;
        if (currWave == 4)
        {
            for(int i = 0; i < 4; i ++) {
                Wither wither = (Wither) location.getWorld().spawnEntity(location, EntityType.WITHER);
                wither.setCustomName(ChatColor.BOLD + "" + ChatColor.DARK_RED + "Dragon's Revenge");
            }
            return;
        }
        if (currWave == 2)
        {
            for(int i = 0; i < 20; i ++) {
                Warden warden = (Warden) location.getWorld().spawnEntity(location, EntityType.WARDEN);
                warden.setCustomName(ChatColor.BOLD + "" + ChatColor.DARK_RED + "Dragon's Revenge");
            }
            return;
        }
        for(int i = 0; i < 100; i ++)
        {
            Silverfish silverfish = (Silverfish) location.getWorld().spawnEntity(location, EntityType.SILVERFISH);
            silverfish.setCustomName(ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "Dragon's Revenge");
            silverfish.addPotionEffect(speedEffect);
        }
        for(int i = 0; i < 20; i ++)
        {
            Blaze blaze = (Blaze) location.getWorld().spawnEntity(location.clone().add(0,20,0), EntityType.BLAZE);
            blaze.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Dragon's Revenge");
            blaze.addPotionEffect(speedEffect);
        }
        for(int i = 0; i < 20; i ++)
        {
            Ghast ghast = (Ghast) location.getWorld().spawnEntity(location.clone().add(0,20,0), EntityType.GHAST);
            ghast.setCustomName(ChatColor.BOLD + "" + ChatColor.DARK_RED + "Dragon's Revenge");
            ghast.addPotionEffect(speedEffect);
        }

    }

}