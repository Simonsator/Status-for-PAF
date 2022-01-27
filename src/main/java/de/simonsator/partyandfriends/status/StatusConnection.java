package de.simonsator.partyandfriends.status;

import de.simonsator.partyandfriends.communication.sql.MySQLData;
import de.simonsator.partyandfriends.communication.sql.pool.PoolData;
import de.simonsator.partyandfriends.communication.sql.pool.PoolSQLCommunication;

import java.sql.*;

/**
 * @author simonbrungs
 * @version 1.0.0 09.01.17
 */
public class StatusConnection extends PoolSQLCommunication {
	private final String TABLE_PREFIX;

	public StatusConnection(MySQLData pMySQLData, PoolData pPoolData) throws SQLException {
		super(pMySQLData, pPoolData);
		TABLE_PREFIX = pMySQLData.TABLE_PREFIX;
		importTable();
	}

	private void importTable() {
		Connection con = getConnection();
		PreparedStatement prepStmt = null;
		try {
			prepStmt = con.prepareStatement("CREATE TABLE IF NOT EXISTS `" + TABLE_PREFIX
					+ "status` (`player_id` INT(8) NOT NULL, `message` varchar(100) NOT NULL);");
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(prepStmt);
		}
	}

	public void setStatus(int pPlayerID, String pMessage) {
		removeStatus(pPlayerID);
		Connection con = getConnection();
		PreparedStatement prepStmt = null;
		try {
			prepStmt = con.prepareStatement(
					"insert into `" + TABLE_PREFIX + "status` values (?, ?)");
			prepStmt.setInt(1, pPlayerID);
			prepStmt.setString(2, pMessage);
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(prepStmt);
		}
	}

	private void removeStatus(int pPlayerID) {
		Connection con = getConnection();
		PreparedStatement prepStmt = null;
		try {
			prepStmt = con.prepareStatement(
					"DELETE FROM `" + TABLE_PREFIX + "status` WHERE player_id = '"
							+ pPlayerID + "' Limit 1");
			prepStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(prepStmt);
		}
	}

	public String getStatus(int pPlayerID) {
		Connection con = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			rs = (stmt = con.createStatement()).executeQuery("SELECT message FROM `"
					+ TABLE_PREFIX + "status` WHERE player_id='" + pPlayerID + "' LIMIT 1");
			if (rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, stmt);
		}
		return null;
	}
}
