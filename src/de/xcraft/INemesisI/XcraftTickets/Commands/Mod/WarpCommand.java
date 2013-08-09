package de.xcraft.INemesisI.XcraftTickets.Commands.Mod;

import java.util.Date;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

@CommandInfo(name = "warp",
		command = "ticket",
		pattern = "w.*",
		permission = "XcraftTickets.Warp",
		usage = "[#]",
		desc = "Teleportiert dich zum Ticket!")
public class WarpCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return true;
		}
		Location loc = ticket.getLoc();
		if (loc.getWorld() == null) {
			loc.setWorld(manager.getPlugin().getServer().createWorld(new WorldCreator(ticket.getWorld())));
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player != null) {
				player.teleport(loc);
				player.performCommand("ticket view " + ticket.getId());
				manager.sendToMods(ticket.getOwner(), ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY + " bearbeitet Ticket "
						+ ChatColor.GOLD + "#" + id);
				manager.sendToPlayer(ticket.getOwner(), "Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wird von "
						+ ChatColor.YELLOW + sender.getName() + ChatColor.GRAY + " bearbeitet!");
				if (ticket.getAssignee() == null) {
					ticket.setAssignee(player.getName());
				}
				manager.setLastTicket(sender, ticket.getId());
				ticket.setProcessed(new Date().getTime());
				// invulnerability for 10 secs
				player.setNoDamageTicks(200);
			}
		} else {
			this.error(sender, "Wie soll ich den Server teleportieren???!? :)");
		}
		return true;
	}
}