package hotl.hcm.commands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;
import hotl.hcm.HCMPlayer;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

public class HCMCommands implements CommandExecutor, TabCompleter {
	private HCM hcm;

	public HCMCommands(HCM hcm) {
		this.hcm = hcm;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (!command.getName().equalsIgnoreCase("hcm")){
			return false;
		}

		if (args.length == 0)
		{
			sendHelpMessage(commandSender);
			return true;
		}

		if (commandSender instanceof Player)
		{
			Player player = (Player) commandSender;

			// mobkills command
			if (args[0].equalsIgnoreCase("mobkills")) {
				if(!hcm.game.isGameRunning() && !hcm.game.isGameFinished())
				{
					player.sendMessage(HCM.formatHCM("Game has not started."));
					return true;
				}
				if(player.isOp())
				{
					player.sendMessage(HCM.formatHCM("" + ChatColor.AQUA + hcm.game.getMobsKilled() + " out of " + hcm.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS + " mobs killed."));
				}
				player.sendMessage(HCM.formatHCM("# of mobs you have killed: " + ChatColor.AQUA + hcm.game.HCMPlayers.get(player.getUniqueId()).getMobKills()));
				return true;
			}

			// time command
			if (args[0].equalsIgnoreCase("time")) {
				if(hcm.game.isGameFinished())
				{
					Duration duration = Duration.between(hcm.game.getGameStartTime(), hcm.game.getGameEndTime());
					player.sendMessage(HCM.formatHCM("Final game duration: " + ChatColor.AQUA + "" + String.format("%d:%02d:%02d", duration.toHours(), duration.toMinutes() % 60, duration.getSeconds() % 60)));
					return true;
				}
				if(!hcm.game.isGameRunning())
				{
					player.sendMessage(HCM.formatHCM("Game has not started."));
					return true;
				}
				Duration duration = Duration.between(hcm.game.getGameStartTime(), LocalDateTime.now());
				player.sendMessage(HCM.formatHCM("Game duration: " + ChatColor.AQUA + "" + String.format("%d:%02d:%02d", duration.toHours(), duration.toMinutes() % 60, duration.getSeconds() % 60)));
				return true;
			}

			// start command
			if (args[0].equalsIgnoreCase("start") && player.isOp()) {
				startGame();
				return true;
			}

			// stop command
			if (args[0].equalsIgnoreCase("stop") && player.isOp()) {
				player.sendMessage(HCM.formatHCM("Game state reset. Use /hcm start to start a new game."));
				hcm.game.resetGame();
				return true;
			}

			// setmobcount command
			if (args.length == 2 && args[0].equalsIgnoreCase("setmobcount") && player.isOp()) {
				int mobcount = Integer.parseInt(args[1]);
				player.sendMessage(HCM.formatHCM("Mob count has been set to " + ChatColor.GREEN + args[1]));
				hcm.REQUIRED_MOB_KILLS_FOR_NETHER_FORTRESS = mobcount;
				return true;
			}

			// setmode command
			if (args.length == 3 && args[0].equalsIgnoreCase("setmode") && player.isOp()) {
				Player targetPlayer = Bukkit.getPlayer(args[1]);
				if (targetPlayer == null)
				{
					player.sendMessage(HCM.formatHCM("Player does not exist."));
					return false;
				}
				HCMPlayer p =hcm.game.HCMPlayers.get(player.getUniqueId());
				p.setPlayerMode(Integer.parseInt(args[2]));
				if (Integer.parseInt(args[2]) == 0) {
					targetPlayer.setGameMode(GameMode.SURVIVAL);
				}
				if (Integer.parseInt(args[2]) == 1) {
					targetPlayer.setGameMode(GameMode.ADVENTURE);
				}
				if (Integer.parseInt(args[2]) == 2) {
					targetPlayer.setGameMode(GameMode.SPECTATOR);
				}
				return true;
			}
		}else {
			commandSender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
			return true;
		}
		sendHelpMessage(commandSender);
		return false;
	}

	private void startGame() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		hcm.game.setGameStartTime(LocalDateTime.now());
		hcm.game.setGameRunning(true);
		World w = Bukkit.getWorld("world");
		w.setTime(1000);
		w.setStorm(false);
		w.setThundering(false);
		w.setWeatherDuration(0);
		for (World world : Bukkit.getWorlds()) {
			world.setDifficulty(Difficulty.HARD);
		}

		for(Player p:Bukkit.getOnlinePlayers())
		{
			hcm.game.HCMPlayers.get(p.getUniqueId()).setPlayerMode(0);
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().clear();
			p.setHealth(p.getAttribute(Attribute.MAX_HEALTH).getValue());
			p.setFoodLevel(20);
			p.setSaturation(20f);

			// Remove potion effects
			for (PotionEffect e : p.getActivePotionEffects())
			{
				p.removePotionEffect(e.getType());
			}

			// Shoot fireworks
			Location loc = p.getLocation().add(0, 4, 0);
			Firework fw = (Firework) p.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
			FireworkMeta meta = fw.getFireworkMeta();

			FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.GREEN).withFade(Color.YELLOW).flicker(true).trail(true).build();

			meta.addEffect(effect);
			meta.setPower(2);
			fw.setFireworkMeta(meta);

			// Play effects for player
			p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
			p.sendMessage(
					HCM.formatHCM("Starting run at " + ChatColor.AQUA + hcm.game.getGameStartTime().format(f)));
			p.sendMessage(HCM.formatHCM("Difficulty is set to " + ChatColor.RED + ChatColor.BOLD + "HARD"));
			p.sendMessage(HCM.formatHCM("Slay the dragon..."));
			p.sendMessage(ChatColor.GREEN + "Use [/hcm mobkills] to view current mob kills.");
			p.sendMessage(ChatColor.GREEN + "Use [/hcm time] to view current speedrun time.");
		}
	}

	private void sendHelpMessage(CommandSender player) {
		// Send help message
		player.sendMessage(ChatColor.GOLD + "=== HCM Help ===");
		player.sendMessage(ChatColor.AQUA + "/hcm mobkills" + ChatColor.WHITE
				+ " - Show the number of mobs you've killed.");
		player.sendMessage(
				ChatColor.AQUA + "/hcm time" + ChatColor.WHITE + " - Show the duration of the game.");
		if (player.isOp()) {
			player.sendMessage(
					ChatColor.AQUA + "/hcm start" + ChatColor.WHITE + " - Start the game (Admin only).");
			player.sendMessage(
					ChatColor.AQUA + "/hcm stop" + ChatColor.WHITE + " - Stop the game (Admin only).");
			player.sendMessage(
					ChatColor.AQUA + "/hcm setmobcount <number>" + ChatColor.WHITE + " - Set the amount of mob kills required for hint (Admin only).");
			player.sendMessage(ChatColor.AQUA + "/hcm setmode <player> <mode>" + ChatColor.WHITE
					+ " - Set the game mode for a player (Admin only).");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<String>();

		// Check if the sender is a player
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;

			// Base command suggestions
			if (args.length == 1) {
				suggestions.addAll(Arrays.asList("mobkills", "time", "start", "stop","setmobcount","setmode"));
			} else if (args.length == 2) {
				// If the command is "setmode", suggest player names
				if (args[0].equalsIgnoreCase("setmode")) {
					for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
						suggestions.add(onlinePlayer.getName());
					}
				}
			} else if (args.length == 3) {
				// If the command is "setmode", suggest mode numbers
				if (args[0].equalsIgnoreCase("setmode")) {
					suggestions.addAll(Arrays.asList("0", "1", "2")); // Game modes
				}
			}
		}
		return suggestions;
	}
}
