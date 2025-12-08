package hotl.hcm;

import hotl.hcm.runnables.EndEffects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import hotl.hcm.commands.HCMCommands;
import hotl.hcm.listeners.EntityDamageListener;
import hotl.hcm.listeners.EntityDeathListener;
import hotl.hcm.listeners.PlayerAchievementAwardedListener;
import hotl.hcm.listeners.PlayerChangeWorldListener;
import hotl.hcm.listeners.PlayerChatListener;
import hotl.hcm.listeners.PlayerDeathListener;
import hotl.hcm.listeners.PlayerInteractListener;
import hotl.hcm.listeners.PlayerJoinListener;
import hotl.hcm.listeners.PlayerMoveListener;
import hotl.hcm.listeners.PlayerRespawnListener;

public class HCM extends JavaPlugin{

	private static int PLAYER_TOTAL = 5;
	
	public int REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS = PLAYER_TOTAL*60;
	public HCMGame game;
	public HCMGameSave gameSave;
	public HCMCommands HCMCommands;

	public static String formatHCM(String msg)
	{
		String header = ChatColor.YELLOW + "" +  ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "HCM" + ChatColor.YELLOW + ChatColor.BOLD + "]";
		return header + " " + ChatColor.GOLD + msg;
	}
	
	@Override
	public void onEnable() 
	{
		game = new HCMGame(this);
		gameSave = new HCMGameSave(this);
		gameSave.loadDataForMatch(Bukkit.getWorld("world").getUID());
		HCMCommands = new HCMCommands(this);
		getCommand("hcm").setExecutor(HCMCommands);
		getCommand("hcm").setTabCompleter(HCMCommands);
		if (!getDataFolder().exists()) getDataFolder().mkdir();
		
		getServer().getPluginManager().registerEvents(new PlayerAchievementAwardedListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerChangeWorldListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
		getServer().getPluginManager().registerEvents(new EntityDeathListener(this), this);
		getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
		getLogger().info("HCM is now enabled...");
		
		//Bukkit.getScheduler().runTaskTimer(this, new DragonBomb(this),0, 20*30);
		Bukkit.getScheduler().runTaskTimer(this, new EndEffects(this), 0, 20*20);
		
	}
	
	@Override
	public void onDisable() 
	{
		gameSave.saveDataForMatch(Bukkit.getWorld("world").getUID());
		getLogger().info("HCM is now disabled...");
	}

}
