package de.xcraft.INemesisI.XcraftTickets.Commands.User;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.XcraftTickets.Log;
import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

@CommandInfo(name = "setwarp",
		command = "ticket",
		pattern = "setw.*|sw",
		permission = "XcraftTickets.Setwarp",
		usage = "[#]",
		desc = "�ndert den Teleport des Tickets")
public class SetWarpCommand extends Command {

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

		if (!this.getName(sender).equals(ticket.getOwner())) {
			this.error(sender, "Du kannst den Warp nur fuer dein eigenes Ticket Aendern!");
			return true;
		}
		if (!(sender instanceof Player)) {
			this.error(sender, "Das ist ueber die Konsole nicht moeglich!");
		} else {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ticket.setLoc(loc);
			ticket.addToLog(new Log(manager.getCurrentDate(), player.getName(), Log.Type.COMMENT, "Der Warppunkt wurde aktualisiert"));
			this.reply(sender, "Der Warp fuer Ticket #" + ticket.getId() + " wurde an deine derzeitige Position verlegt!");
			ticket.clearWatched();
		}
		return true;
	}
}