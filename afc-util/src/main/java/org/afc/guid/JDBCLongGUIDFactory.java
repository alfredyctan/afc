package org.afc.guid;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCLongGUIDFactory extends JDBCGUIDFactory<Long> {

	private static final Logger logger = LoggerFactory.getLogger(JDBCLongGUIDFactory.class);

	private Object LOCK = new Object();

	private long seed;
	
	private long ceiling;

	private int lot;
	
	private int idColumn;
	
	public JDBCLongGUIDFactory(DataSource dataSource, int lot, int idColumn, String... queries) {
		super(dataSource, queries);
		this.seed = 0;
		this.ceiling = -1;
		this.idColumn = idColumn;
		this.lot = lot;
		refresh();
	}

	@Override
	public Long generate() {
		synchronized(LOCK) {
			if (seed > ceiling) {
				refresh();
			}
			return seed++; 
		}
	}

	@Override
	protected void processResultSet(ResultSet resultSet) throws SQLException {
		seed = resultSet.getLong(idColumn);
		ceiling = seed + lot - 1;
		logger.info("ID seed renewed, seed:[{}], ceiling:[{}]", seed, ceiling);
	}
}
