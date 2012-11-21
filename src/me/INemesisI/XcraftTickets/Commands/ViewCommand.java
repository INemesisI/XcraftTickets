package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ViewCommand extends CommandHelper {

	protected ViewCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = th.getTicket(id);
		if (ticket == null) ticket = th.getArchivedTicket(Integer.parseInt(list.get(0)));
		if (ticket == null) {
			error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		if (!ticket.owner.equals(getName()) && !senderHasPermission("View.All")) {
			error("Du hast keine Rechte dieses Ticket zu sehen!");
			return;
		}
		reply(ChatColor.GREEN + "info für Ticket " + ChatColor.GOLD + "#" + ticket.id);
		if (ticket.assignee != null) sender.sendMessage(ChatColor.GOLD + "Zugewiesen an: " + ChatColor.RED + ticket.assignee);
		for (int i = 0; i < ticket.log.size(); i++) {
			sender.sendMessage(ticket.log.get(i).format());
		}
		ticket.watched.add(getName());
		plugin.configHandler.removeReminder(ticket.owner, ticket.id);
	}
}
