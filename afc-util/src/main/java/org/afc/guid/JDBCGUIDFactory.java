package org.afc.guid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.AFCException;
import org.afc.jdbc.AutoTx;

public abstract class JDBCGUIDFactory<T> implements GUIDFactory<T> {

	private static final Logger logger = LoggerFactory.getLogger(JDBCGUIDFactory.class);

	private DataSource dataSource;
	
	private String[] queries;
	
	public JDBCGUIDFactory(DataSource dataSource, String... queries) {
		this.dataSource = dataSource;
		this.queries = queries;
	}	
	
	protected void refresh() {
		try (Connection conn = dataSource.getConnection(); AutoTx tx = new AutoTx(conn)) {
		    conn.setAutoCommit(false);
		    
			for (int i = 0; i < queries.length - 1; i++) {
				PreparedStatement stmt = conn.prepareStatement(queries[i]);
				stmt.execute();
				logger.info("executed : {}", queries[i]);
			}
			
			PreparedStatement stmt = conn.prepareStatement(queries[queries.length - 1]);
			ResultSet rs = stmt.executeQuery();
			logger.info("executed : {}", queries[queries.length - 1]);
			if (rs.next()) {
				processResultSet(rs);
			} else {
				throw new AFCException("failed to renew id seed, result set not found. details : " + Arrays.asList(queries));
			}

			tx.commit();
		} catch (SQLException e) {
			throw new AFCException("failed to renew id seed, details : " + Arrays.asList(queries), e);
		}
	}
	
	protected abstract void processResultSet(ResultSet resultSet) throws SQLException;
	
}
