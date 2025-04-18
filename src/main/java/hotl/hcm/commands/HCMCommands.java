package hotl.hcm.commands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import hotl.hcm.HCM;
import hotl.hcm.HCMGame;
import hotl.hcm.HCMPlayer;

public class HCMCommands implements CommandExecutor, TabCompleter {
	private HCM plugin;
	private HCMGame game;

	public HCMCommands(HCM plugin) {
		this.plugin = plugin;
		this.game = plugin.game;
	}

	// This method is called, when somebody uses our command
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && command.getName().equalsIgnoreCase("hcm")) {
			Player player = (Player) sender;
			if (args.length > 0 && args[0].equalsIgnoreCase("mobkills")) {
				player.sendMessage(HCM.formatHCM("# of mobs you have killed: " + ChatColor.AQUA
						+ game.HCMPlayers.get(player.getUniqueId()).getMobKills()));
				return true;
			}
			if (args.length > 0 && args[0].equalsIgnoreCase("time")) {
				Duration duration = Duration.between(game.getGameStartTime(), LocalDateTime.now());
				player.sendMessage(HCM.formatHCM("Game Duration: " + ChatColor.AQUA + duration.toHours() + ":"
						+ duration.toMinutes() % 60 + ":" + duration.getSeconds() % 60));
				return true;
			}
			if (args.length > 0 && args[0].equalsIgnoreCase("start") && sender.isOp()) {
				DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				game.setGameStartTime(LocalDateTime.now());
				player.sendMessage(
						HCM.formatHCM("Starting game at " + ChatColor.AQUA + game.getGameStartTime().format(f)));
				game.setGameRunning(true);
				return true;
			}
			if (args.length > 2 && args[0].equalsIgnoreCase("setmode") && sender.isOp()) {
				HCMPlayer p = game.HCMPlayers.get(Bukkit.getPlayer(args[1]).getUniqueId());
				p.setPlayerMode(Integer.parseInt(args[2]));
				if (Integer.parseInt(args[2]) == 0) {
					p.getPlayer().setGameMode(GameMode.SURVIVAL);
				}
				if (Integer.parseInt(args[2]) == 1) {
					p.getPlayer().setGameMode(GameMode.ADVENTURE);
				}
				if (Integer.parseInt(args[2]) == 2) {
					p.getPlayer().setGameMode(GameMode.SPECTATOR);
				}
				return true;
			}
			if (args.length == 0) {

				// Send help message
				player.sendMessage(ChatColor.GOLD + "=== HCM Help ===");
				player.sendMessage(ChatColor.AQUA + "/hcm mobkills" + ChatColor.WHITE
						+ " - Show the number of mobs you've killed.");
				player.sendMessage(
						ChatColor.AQUA + "/hcm time" + ChatColor.WHITE + " - Show the duration of the game.");
				if (player.isOp()) {
					player.sendMessage(
							ChatColor.AQUA + "/hcm start" + ChatColor.WHITE + " - Start the game (Admin only).");
					player.sendMessage(ChatColor.AQUA + "/hcm setmode <player> <mode>" + ChatColor.WHITE
							+ " - Set the game mode for a player (Admin only).");
				}
				return true;
			}

		} else {
			// If the sender is not a player (e.g., console), send a message
			sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
			return true;
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<String>();

		// Check if the sender is a player
		if (sender instanceof Player) {
			Player player = (Player) sender;

			// Base command suggestions
			if (args.length == 1) {
				suggestions.addAll(Arrays.asList("mobkills", "time", "start", "setmode"));
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

		// Return filtered suggestions
		return suggestions;
	}
}
