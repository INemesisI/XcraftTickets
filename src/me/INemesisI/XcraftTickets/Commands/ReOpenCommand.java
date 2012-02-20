package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReOpenCommand extends CommandHelper {

	protected ReOpenCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket reopen <Nr> <Nachricht>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		String message = list.subList(1, list.size()).toString().replace(",", "").replace("[", "").replace("]", "");
		Ticket ticket = th.getArchivedTicket(id);
		if (ticket == null) {
			error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
		} else {
			th.addTicket(ticket);
			th.LogTicket(ticket, getName(), "wieder eröffnet", message);
			ticket.getWatched().add(getName());
			sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde wieder eröffnet: " + ChatColor.AQUA + message);
			sendToMods(ChatColor.GRAY + "Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde wieder eröffnet: " + ChatColor.AQUA + message);
			return;
		}

	}
}
