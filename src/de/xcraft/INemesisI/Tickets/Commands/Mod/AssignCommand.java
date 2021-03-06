package de.xcraft.INemesisI.Tickets.Commands.Mod;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Tickets.Log.EntryType;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.Msg.Replace;
import de.xcraft.INemesisI.Tickets.Ticket;
import de.xcraft.INemesisI.Tickets.Manager.TicketManager;

public class AssignCommand extends XcraftCommand {

	public AssignCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
		super(cManager, command, name, pattern, usage, desc, permission);
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Replace.ID(id)), true);
			return true;
		}
		if (!manager.getAssignees().contains(args[1])) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_ASSIGNEE_NOT_FOUND.toString(Replace.NAME(args[1])), true);
			return true;
		} else {
			ticket.getLog().add(EntryType.ASSIGN, sender.getName(), args[1]);

			if (ticket.getId() == manager.getLastTicket(sender)) {
				manager.setLastTicket(sender, -1);
			}
			ticket.setAssignee(args[1]);
			Replace[] replace = { Replace.ID(ticket.getId()), Replace.NAME(sender.getName()), Replace.ASSIGNEE(args[1]) };
			manager.inform(ticket, Msg.TICKET_BROADCAST_ASSIGN.toString(replace), true);
			return true;
		}
	}
}
