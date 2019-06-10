package org.afc.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoTx implements AutoCloseable {

	private static final Logger logger = LoggerFactory.getLogger(AutoTx.class);

	private boolean autoCommit;

	private boolean committed;

	private Connection connection;

	public AutoTx(Connection connection) throws SQLException {
		this.connection = connection;
		this.autoCommit = connection.getAutoCommit();
		this.committed = false;
	}

	public void commit() throws SQLException {
		connection.commit();
		committed = true;
	}

	@Override
	public void close() {
		try {
			if (!committed) {
				connection.rollback();
				logger.error("rollback transaction");
			}
		} catch (SQLException e) {
			logger.error("failed to rollback transaction", e);
		}
		try {
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			logger.error("failed to restore auto commit setting to [" + autoCommit + ']', e);
		}
	}
}
