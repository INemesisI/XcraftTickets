package de.xcraft.INemesisI.XcraftTickets.Commands.Mod;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.XcraftTickets.Log;
import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

@CommandInfo(name = "assign",
		command = "ticket",
		pattern = "a.*",
		permission = "XcraftTickets.Assign",
		usage = "[#] [Name|Gruppe]",
		desc = "Weiterleiten eines Tickets")
public class AssignCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}
		if (args.length < 2) {
			this.error(sender, "Du hast keine(n) Namen/Gruppe eingeben!");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return true;
		}

		if (!manager.getAssignees().contains(args[1])) {
			this.error(sender, "Das Ticket kann nicht an diese Person/Gruppe weitergeleitet werden!");
			return true;
		} else {

			ticket.addToLog(new Log(manager.getCurrentDate(), this.getName(sender), Log.Type.ASSIGN, args[1]));
			if (ticket.getId() == manager.getLastTicket(sender)) {
				manager.setLastTicket(sender, -1);
			}
			ticket.setAssignee(args[1]);
			manager.sendToMods(ticket.getOwner(), ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY
					+ " wurde von " + ChatColor.YELLOW + sender.getName() + ChatColor.GRAY + " an " + ChatColor.DARK_PURPLE + args[1]
					+ ChatColor.GRAY + " zugewiesen!");
			manager.sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY
					+ " wurde von " + ChatColor.YELLOW + sender.getName() + ChatColor.GRAY + " an " + ChatColor.DARK_PURPLE + args[1]
					+ ChatColor.GRAY + " zugewiesen!");
			return true;
		}
	}
}