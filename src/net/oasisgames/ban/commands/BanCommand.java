package net.oasisgames.ban.commands;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.oasisgames.ban.time.Conversion;

public class BanCommand implements CommandExecutor {

	private final String failedCommandMsg;
	private final String offlinePlayerMsg;
	
	public BanCommand(FileConfiguration config) {
		failedCommandMsg = ChatColor.translateAlternateColorCodes('&', config.getString("messages.failedCommand"));
		offlinePlayerMsg = ChatColor.translateAlternateColorCodes('&', config.getString("messages.offlinePlayer"));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//IF SENDER IS PLAYER
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!label.equalsIgnoreCase("ban") && !label.equalsIgnoreCase("tempban")) return true;
			if (args.length < 3) {
				player.sendMessage(failedCommandMsg);
				return true;
			}
			//Attempt to find online player
			try {
				Player target = Bukkit.getPlayer(args[0]);
				Duration banLength = Conversion.convertStringToTime(args[1]);
				String reason = "";
				for (int i = 2; i < args.length; i++) {
					reason += args[i] + "\s";
				}
				player.kickPlayer(target.getName());
				player.sendMessage("Successfully banned: " + target.getName() + ", for duration of: " + banLength.toString() + ", and the reason of: " + reason); //TODO
			} catch (NullPointerException e) {
				player.sendMessage(offlinePlayerMsg);
				return true;
			}
		//IF SENDER NOT PLAYER
		} else {
			if (!label.equalsIgnoreCase("ban") && !label.equalsIgnoreCase("tempban")) return true;
			if (args.length < 3) {
				Bukkit.getLogger().info(failedCommandMsg);
				return true;
			}
			//Attempt to find online player
			try {
				Player target = Bukkit.getPlayer(args[0]);
				Duration banLength = Conversion.convertStringToTime(args[1]);
				String reason = "";
				for (int i = 2; i < args.length; i++) {
					reason += args[i] + "\s";
				}
				Bukkit.getLogger().info("Successfully banned: " + target.getName() + ", for duration of: " + Conversion.durationToString(banLength) + ", and the reason of: " + reason); //TODO
			} catch (NullPointerException e) {
				Bukkit.getLogger().info(offlinePlayerMsg);
				return true;
			}
		}
		
		return false;
	}
	
}
