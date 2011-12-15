package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

public class ViewCommand extends CommandHelper{

	protected ViewCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.setSender(sender);
		
		
		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben"+ChatColor.GRAY+"(/ticket view <Nr>)");
			return;
		}
		Ticket ticket = th.getTicket(Integer.parseInt(list.get(0)));
		
		if (ticket == null) {
			ticket = th.getArchievedTicket(Integer.parseInt(list.get(0)));
			if (ticket == null) {
				error("Ein Ticket mit dieser Nummer existiert nicht!");
				return;
			}
		}
		
		if (ticket.getOwner().equals(sender.getName()) || senderHasPermission("View.Other")) {
			error("Du hast keine Rechte dieses Ticket zu sehen!"+ChatColor.GRAY+"  Es ist nicht dein Ticket...");
			return;
		}
		
		reply(ChatColor.GREEN+"info f�r Ticket " +ChatColor.GOLD+"#"+ticket.getId() +"  "+ChatColor.GRAY+ticket.getDate());
		String marker = null;
		if (plugin.getServer().getOfflinePlayer(ticket.getOwner()).isOnline()) {
			marker = ChatColor.DARK_GREEN+"+";
		} else {
			marker = ChatColor.DARK_RED+"-";
		}
		
		reply(ChatColor.GOLD+"Ticket opened "+marker+ChatColor.WHITE+ticket.getOwner()+": "+ChatColor.GRAY+ticket.getLog().get(0));

		if (ticket.getAssignee() != null)
			reply(ChatColor.GOLD+"Assigned to: "+ChatColor.RED+ticket.getAssignee());
		List<String> logs = ticket.getLog();
		for(String log : logs) {
			log = log.replace("comment by ", "");
			String[] split = log.split("\\|", 2);
			String time = split[0];
			split = split[1].split(":", 2);
			String info = split[0];
			String out = split[1];
			reply(ChatColor.BLUE+"-> "+ChatColor.DARK_GRAY+time+ChatColor.WHITE+"|"+ChatColor.YELLOW+info+ChatColor.WHITE+out);
		}
		ticket.getWatched().add(sender.getName());
	}
}