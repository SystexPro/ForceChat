package com.bukkit.fc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class ForceChat extends JavaPlugin {

	private boolean UsePermissions;
	public  PermissionHandler permissionHandler;
	public String pd = null;

	@Override
	public void onDisable() {
		send("Disabled");
	}

	@Override
	public void onEnable() {
		send("Enabled");
		setupPermissions();
	}

	/**
	 * Handle Commands
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("fc")) {
			if (args.length >= 2 && sender instanceof Player) {
				handleCommand(sender, args);
				return true;
			} else {
				return help(sender);
			}
		}
		if (commandLabel.equalsIgnoreCase("force")) {
			if (args.length >= 1 && sender instanceof Player) {
				Player forceplayer = this.getServer().getPlayer(pd);
				Player forcer = (Player) sender;
				send(forcer.getDisplayName() + " forced " + pd + " to say " + append(args, " "));
				forceplayer.chat(append(args, " "));
				return true;
			} else {
				return help(sender);
			}
		}
		return true;
	}

	/**
	 * Used to handle the FC Comamnds
	 * @param sender
	 * @param args
	 * @return
	 */
	private boolean handleCommand(CommandSender sender, String[] args) {
		String cmd = args[0];
		Player player = (Player) sender;
		if(cmd.equalsIgnoreCase("help")) {
			return help(sender);
		} else if(cmd.equalsIgnoreCase("attach") && this.isAdmin(player)) {
			pd = (String) args[1];
			player.sendMessage(ChatColor.AQUA + "Attaching to player (" + pd + ")");
			return true;
		} 
		return true;

	}

	/**
	 * Appending Text from a Array
	 * @param parts
	 * @param delimeter
	 * @return
	 */
	public String append(String[] parts, String delimeter)
	{
		StringBuilder builder;
		int i;

		if (parts.length == 0)
			return null;

		builder = new StringBuilder();

		for (i = 0; i < parts.length - 1; i++)
		{
			builder.append(parts[i]);
			builder.append(delimeter);
		}

		builder.append(parts[i]);

		return builder.toString();
	}


	private void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

		if (this.permissionHandler == null) {
			if (permissionsPlugin != null) {
				this.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
				send("Permissions detected. Hooking into API");
				UsePermissions = true;
			} else {
				send("Permission system not detected, defaulting to OP");
				UsePermissions = false;
			}
		}
	}

	/**
	 * Checks wether to default to permissions or Ops;
	 * @param p
	 * @return
	 */
	public boolean isAdmin(Player p) {
		if (UsePermissions) {
			return this.permissionHandler.has(p, "forcechat.use");
		}
		return p.isOp();
	}

	public boolean help(CommandSender sender) {
		Player p = (Player) sender;
		p.sendMessage(ChatColor.YELLOW + "==ForceChat==");
		p.sendMessage(ChatColor.RED + "[]" + ChatColor.YELLOW + " - Required" + ChatColor.GREEN + " ()" + ChatColor.YELLOW + " - Optional");
		p.sendMessage(ChatColor.AQUA + "/fc attach [playername] - " + ChatColor.YELLOW + "Attachs FC to that username.");
		p.sendMessage(ChatColor.AQUA + "/force [message] - " + ChatColor.YELLOW + "Forces the Player to say the message");
		p.sendMessage(ChatColor.YELLOW + "(Note) Player must be online or there will be a Error");
		return true;
	}

	/**
	 * CMD Format
	 * @param text
	 */
	public void send(String text) {
		System.out.println("[ForceChat] " + text);
	}

}
