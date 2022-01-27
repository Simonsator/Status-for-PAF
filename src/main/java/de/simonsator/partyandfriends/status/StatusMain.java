package de.simonsator.partyandfriends.status;

import de.simonsator.partyandfriends.api.PAFExtension;
import de.simonsator.partyandfriends.api.events.communication.spigot.SendDataFriendEvent;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.communication.sql.MySQLData;
import de.simonsator.partyandfriends.communication.sql.pool.PoolData;
import de.simonsator.partyandfriends.friends.commands.Friends;
import de.simonsator.partyandfriends.main.Main;
import de.simonsator.partyandfriends.pafplayers.mysql.PAFPlayerMySQL;
import de.simonsator.partyandfriends.status.commands.StatusTopCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class StatusMain extends PAFExtension implements Listener {
	private StatusConnection connection;

	@Override
	public void onEnable() {
		try {
			Configuration config = new StatusConfigurationCreator(new File(getConfigFolder(), "config.yml"), this).getCreatedConfiguration();
			PoolData poolData = new PoolData(Main.getInstance().getGeneralConfig().getInt("MySQL.Pool.MinPoolSize"),
					Main.getInstance().getGeneralConfig().getInt("MySQL.Pool.MaxPoolSize"),
					Main.getInstance().getGeneralConfig().getInt("MySQL.Pool.InitialPoolSize"), Main.getInstance().getGeneralConfig().getInt("MySQL.Pool.IdleConnectionTestPeriod"),
					Main.getInstance().getGeneralConfig().getBoolean("MySQL.Pool.TestConnectionOnCheckin"), Main.getInstance().getGeneralConfig().getString("MySQL.Pool.ConnectionPool"));
			connection = new StatusConnection(new MySQLData(Main.getInstance().getGeneralConfig().getString("MySQL.Host"),
					Main.getInstance().getGeneralConfig().getString("MySQL.Username"), Main.getInstance().getGeneralConfig().getString("MySQL.Password"),
					Main.getInstance().getGeneralConfig().getInt("MySQL.Port"), Main.getInstance().getGeneralConfig().getString("MySQL.Database"),
					Main.getInstance().getGeneralConfig().getString("MySQL.TablePrefix"), Main.getInstance().getGeneralConfig().getBoolean("MySQL.UseSSL")), poolData);
			ProxyServer.getInstance().getPluginManager().registerCommand(this,
					new StatusTopCommand(config.getStringList("Commands.TopCommands.Status.Names").toArray(new String[1]), config.getString("Commands.TopCommands.Status.Permission"), Friends.getInstance().getPrefix(), config, this));
			getAdapter().registerListener(this, this);
			registerAsExtension();
		} catch (IOException | SQLException e) {
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
		String status = getStatus(pEvent.getPlayerListElement().getPlayer());
		if (status != null)
			pEvent.addProperty("status", status);
	}
}
