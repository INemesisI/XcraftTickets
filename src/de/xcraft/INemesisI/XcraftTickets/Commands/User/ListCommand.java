package de.xcraft.INemesisI.XcraftTickets.Commands.User;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

@CommandInfo(name = "list", command = "ticket", pattern = "li.*", permission = "XcraftTickets.List", usage = "", desc = "Listet alle deine Tickets auf")
public class ListCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		int counter = -1;
		for (Ticket ticket : manager.getTickets()) {
			//@formatter:off
				// tickets from owner
			if (ticket.getOwner().equals(this.getName(sender))
			|| (args.length > 0 && args[0].matches("a.*") && sender.hasPermission("XcraftTickets.List.All"))
				// sender is console...
			|| (sender instanceof ConsoleCommandSender)
				// player is allowed to see all tickets. except of assigned ones
			|| (ticket.getAssignee() == null && sender.hasPermission("XcraftTickets.View.All"))
				// ticket is assigned to the player
			|| (ticket.getAssignee() != null && (ticket.getAssignee().equals(this.getName(sender))
				// or assignee is a group and player is in group
			|| manager.getPlugin().getPermission().playerInGroup((Player) sender, ticket.getAssignee())))) {
			//@formatter:on
				if (counter == -1) {
					sender.sendMessage(manager.getPlugin().getCName() + "Ticketliste");
				}
				counter++;
				int c = ticket.getLog().size() - 1;
				String comments;
				if (ticket.hasWatched(this.getName(sender))) {
					comments = ChatColor.GRAY + "[" + c + "]";
				} else {
					comments = ChatColor.DARK_AQUA + "[" + c + "]";
				}
				if (c == 0) {
					if (!ticket.hasWatched(this.getName(sender))) {
						ticket.addToWatched(this.getName(sender));
					}
				}
				sender.sendMessage(manager.getTicketInfo(ticket) + " " + comments);
			}
		}
		if (counter == -1) {
			this.reply(sender, "Es gibt keine offenen Tickets!");
		}
		return true;
	}
}