package de.simonsator.partyandfriends.status;

import de.simonsator.partyandfriends.api.events.communication.spigot.SendDataFriendEvent;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.communication.sql.MySQLData;
import de.simonsator.partyandfriends.friends.commands.Friends;
import de.simonsator.partyandfriends.main.Main;
import de.simonsator.partyandfriends.pafplayers.mysql.PAFPlayerMySQL;
import de.simonsator.partyandfriends.status.commands.StatusTopCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;

/**
 * @author simonbrungs
 * @version 1.0.0 09.01.17
 */
public class StatusMain extends Plugin implements Listener {
	private StatusConnection connection;

	@Override
	public void onEnable() {
		try {
			Configuration config = new StatusConfigurationCreator(new File(getDataFolder(), "config.yml")).getCreatedConfiguration();
			connection = new StatusConnection(new MySQLData(Main.getInstance().getConfig().getString("MySQL.Host"),
					Main.getInstance().getConfig().getString("MySQL.Username"), Main.getInstance().getConfig().getString("MySQL.Password"),
					Main.getInstance().getConfig().getInt("MySQL.Port"), Main.getInstance().getConfig().getString("MySQL.Database"),
					Main.getInstance().getConfig().getString("MySQL.TablePrefix")));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,
					new StatusTopCommand(config.getStringList("Commands.TopCommands.Status.Names").toArray(new String[1]), config.getString("Commands.TopCommands.Status.Permission"), Friends.getInstance().getPrefix(), config, this));
			ProxyServer.getInstance().getPluginManager().registerListener(this, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStatus(OnlinePAFPlayer pPlayer, String pMessage) {
		connection.setStatus(((PAFPlayerMySQL) pPlayer.getPAFPlayer()).getPlayerID(), pMessage);
	}

	public String getStatus(PAFPlayer pPlayer) {
		return connection.getStatus(((PAFPlayerMySQL) pPlayer.getPAFPlayer()).getPlayerID());
	}

	@EventHandler
	public void onSendDataFriend(SendDataFriendEvent pEvent) {
		String status = getStatus(pEvent.getPAFFriend());
		if (status != null)
			pEvent.addProperty("status", status);
	}
}
