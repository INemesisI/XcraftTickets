package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.INemesisI.XcraftTickets.XcraftTickets;

public class PluginCommand extends CommandHelper{

	protected PluginCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.setSender(sender);
		
		if(Command.equals("reload")) {
			//TODO: do some stuff
			reply("Not implemented yet");
		}
		
		if(Command.equals("save")) {
			//TODO: do some stuff
			reply("Not implemented yet");
		}
	}

}
