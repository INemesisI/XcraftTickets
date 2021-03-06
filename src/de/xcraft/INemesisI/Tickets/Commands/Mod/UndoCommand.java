package de.xcraft.INemesisI.Tickets.Commands.Mod;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Tickets.Log.LogEntry;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.Msg.Replace;
import de.xcraft.INemesisI.Tickets.Ticket;
import de.xcraft.INemesisI.Tickets.Manager.TicketManager;

public class UndoCommand extends XcraftCommand {


	public UndoCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
		super(cManager, command, name, pattern, usage, desc, permission);
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			ticket = manager.getArchivedTicket(id);
		}
		if (ticket == null) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Replace.ID(id)), true);
			return true;
		} else {
			LogEntry entry = ticket.getLog().getEntry(ticket.getLog().size() - 1);
			if (!entry.player.equals(sender.getName())) {
				pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_UNDO_IMPOSSIBLE.toString(), true);
			} else {
				boolean done = false;
				switch (entry.type) {
				case OPEN:
					manager.deleteTicket(ticket);
					done = true;
				case COMMENT:
					ticket.getLog().remove(entry);
					done = true;
				case REOPEN:
					ticket.getLog().remove(entry);
					manager.setTicketArchived(ticket);
					done = true;
				case CLOSE:
					ticket.getLog().remove(entry);
					manager.addTicket(ticket);
					done = true;
				case ASSIGN:
					ticket.getLog().remove(entry);
					ticket.setAssignee(null);
					done = true;
				case SETWARP:
					pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_UNDO_IMPOSSIBLE.toString(), true);
					return true;
				}
				if (done) {
					pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_UNDO_SUCCESSFUL.toString(), true);
					return true;
				} else
					return false;
			}
		}
		return true;
	}
}
