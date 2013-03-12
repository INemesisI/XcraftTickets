package me.INemesisI.XcraftTickets.Commands.User;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "SetWarp",
		command = "ticket|t",
		pattern = "setw.*|sw",
		permission = "XcraftTickets.Setwarp",
		usage = "/ticket setwarp<#>",
		desc = "�ndert den Ort, an dem das Ticket erstellt wurde ab")
public class SetWarpCommand extends Command {

	protected SetWarpCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getArchivedTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED
					+ " konnte nicht gefunden werden");
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
			ticket.getLog().add(
					new Log(manager.getCurrentDate(), player.getName(), Log.Type.COMMENT,
							"Der Warppunkt wurde aktualisiert"));
			this.reply(sender, "Der Warp für Ticket #" + ticket.getId()
					+ " wurde an deine derzeitige Position verlegt!");
		}
		return true;
	}

}