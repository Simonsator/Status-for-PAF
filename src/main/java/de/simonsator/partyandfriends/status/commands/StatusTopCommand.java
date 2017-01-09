package de.simonsator.partyandfriends.status.commands;

import de.simonsator.partyandfriends.api.TopCommand;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.status.StatusMain;
import net.md_5.bungee.config.Configuration;

/**
 * @author simonbrungs
 * @version 1.0.0 09.01.17
 */
public class StatusTopCommand extends TopCommand {
	private final String TOO_LESS_ARGUMENTS;
	private final String MESSAGE_TOO_LONG;
	private final String STATUS_WAS_SET;
	private final StatusMain PLUGIN;

	public StatusTopCommand(String[] pCommandNames, String pPermission, String pPrefix, Configuration pConfig, StatusMain pPlugin) {
		super(pCommandNames, pPermission, pPrefix);
		PLUGIN = pPlugin;
		TOO_LESS_ARGUMENTS = pConfig.getString("Messages.TooLessArguments");
		MESSAGE_TOO_LONG = pConfig.getString("Messages.MessageTooLong");
		STATUS_WAS_SET = pConfig.getString("Messages.StatusWasSet");
	}

	@Override
	protected void onCommand(OnlinePAFPlayer pPlayer, String[] args) {
		if (args.length == 0) {
			pPlayer.sendMessage(getPrefix() + TOO_LESS_ARGUMENTS);
			return;
		}
		String message = toString(args);
		if (message.length() > 100) {
			pPlayer.sendMessage(getPrefix() + MESSAGE_TOO_LONG);
			return;
		}
		PLUGIN.setStatus(pPlayer, message);
		pPlayer.sendMessage(getPrefix() + STATUS_WAS_SET);
	}

	private String toString(String[] args) {
		StringBuilder content = new StringBuilder();
		for (int n = 0; n < args.length; ++n) {
			content.append(" ");
			content.append(args[n]);
		}
		return content.toString();
	}
}
