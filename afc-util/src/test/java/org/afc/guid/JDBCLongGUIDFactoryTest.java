package org.afc.guid;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import org.afc.util.JUnitUtil;
import com.zaxxer.hikari.HikariDataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.NONE, replace = Replace.NONE)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JDBCLongGUIDFactoryTest {

	@Autowired
	@Qualifier("testDataSource")
	private HikariDataSource dataSource;

	@Test
	public void testSequenceBased() throws Exception {
		GUIDFactory<Long> factory = new JDBCLongGUIDFactory(dataSource, 3, 1, "select nextval('test_id_seq')");
		int size = 10;
		List<Long> actual = new LinkedList<>();
		for(int i = 0; i < size; i++) {
			actual.add(factory.generate());
		}
		JUnitUtil.actual(actual);
		
		List<Long> expect = new LinkedList<>();
		for(long i = 1; i <= size; i++) {
			expect.add(i);
		}
		JUnitUtil.expect(expect);
		
		assertThat("id match", actual, containsInAnyOrder(expect.toArray()));
	}

	public static <T> List<T> construct(List<T> out, int n, Consumer<T> accept, Supplier<T> supply) {
		for (int i = 0; i < n; i++) {
			accept.accept(supply.get());
		}
		return out;
	}
	
	@Test
	public void testIdentityBased() throws Exception {
		GUIDFactory<Long> factory = new JDBCLongGUIDFactory(
			dataSource, 3, 1, 
			"DELETE FROM test_identity WHERE name = 'trade'", 
			"INSERT INTO test_identity(name) VALUES ('trade')", 
			"SELECT id FROM test_identity WHERE name = 'trade'"
		);

		int size = 10;
		List<Long> actual = new LinkedList<>();
		for(int i = 0; i < size; i++) {
			actual.add(factory.generate());
		}
		JUnitUtil.actual(actual);
		
		List<Long> expect = new LinkedList<>();
		for(long i = 1; i <= size; i++) {
			expect.add(i);
		}
		JUnitUtil.expect(expect);
		
		assertThat("id match", actual, containsInAnyOrder(expect.toArray()));
	}

	@Test
	public void testIdentityConcurrency() throws Exception {
		final GUIDFactory<Long> factory = new JDBCLongGUIDFactory(dataSource, 3, 1, "select nextval('test_id_seq_concurrency')");
		
		int threadSize = 10;
		int size = 1000;
		List<Long> actuals[] = new LinkedList[threadSize];
		Thread[] threads = new Thread[threadSize];
		CountDownLatch count = new CountDownLatch(threadSize);
		for (int i = 0; i < threadSize; i++) {
			actuals[i] = new LinkedList<>();
			threads[i] = new Thread(new ThreadGetter(factory, actuals[i], size, count));
		}

		for (int i = 0; i < threadSize; i++) {
			threads[i].start();
		}
		count.await(10, TimeUnit.SECONDS);
		
		List<Long> actual = new LinkedList<>();
		for (List<Long> list:actuals) {
			actual.addAll(list);
		}
		JUnitUtil.actual(actual.size());
		
		List<Long> expect = new LinkedList<>();
		for(long i = 1; i <= size * threadSize; i++) {
			expect.add(i);
		}
		JUnitUtil.expect(expect.size());
		
		assertThat("id match", actual, containsInAnyOrder(expect.toArray()));
	}

	@Test
	public void testSequenceConcurrency() throws Exception {
		final GUIDFactory<Long> factory = new JDBCLongGUIDFactory(
			dataSource, 3, 1, 
			"DELETE FROM test_identity_concurrency WHERE name = 'trade'", 
			"INSERT INTO test_identity_concurrency(name) VALUES ('trade')", 
			"SELECT id FROM test_identity_concurrency WHERE name = 'trade'"
		);
		
		int threadSize = 10;
		int size = 1000;
		List<Long> actuals[] = new LinkedList[threadSize];
		Thread[] threads = new Thread[threadSize];
		CountDownLatch count = new CountDownLatch(threadSize);
		for (int i = 0; i < threadSize; i++) {
			actuals[i] = new LinkedList<>();
			threads[i] = new Thread(new ThreadGetter(factory, actuals[i], size, count));
		}

		for (int i = 0; i < threadSize; i++) {
			threads[i].start();
		}
		count.await(10, TimeUnit.SECONDS);
		
		List<Long> actual = new LinkedList<>();
		for (List<Long> list:actuals) {
			actual.addAll(list);
		}
		JUnitUtil.actual(actual.size());
		
		List<Long> expect = new LinkedList<>();
		for(long i = 1; i <= size * threadSize; i++) {
			expect.add(i);
		}
		JUnitUtil.expect(expect.size());
		
		assertThat("id match", actual, containsInAnyOrder(expect.toArray()));
	}
		
	static class ThreadGetter implements Runnable {
		
		private GUIDFactory<Long> factory;
		
		private List<Long> ids;
		
		private int size;
		
		private CountDownLatch count;
		
		public ThreadGetter(GUIDFactory<Long> factory, List<Long> ids, int size, CountDownLatch count) {
			this.factory = factory;
			this.ids = ids;
			this.size = size;
			this.count = count;
		}
		
		@Override
		public void run() {
			for (int n = 0; n < size; n++) {
				ids.add(factory.generate());	
			}
			count.countDown();
		}
	}	
	
	
	@Configuration
	public static class TestConfig {

		@Bean
		@Primary
		@ConfigurationProperties(prefix = "test.datasource")
		public DataSourceProperties testDataSourceProperties() {
			return new DataSourceProperties();
		}

		@Bean
		@ConfigurationProperties(prefix = "test.datasource")
		public HikariDataSource testDataSource(@Qualifier("testDataSourceProperties") DataSourceProperties testDataSourceProperties) {
			HikariDataSource testDataSource = testDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
			return testDataSource;
		}

		@Value("classpath:schema/test/hsqldb/1.0.0/schema/test_jdbc_id.schema.sql")
		private Resource schemaScript;

		@Bean
		public DataSourceInitializer dataSourceInitializer(@Qualifier("testDataSource") HikariDataSource dataSource) {
			DataSourceInitializer initializer = new DataSourceInitializer();
			initializer.setDataSource(dataSource);
			initializer.setDatabasePopulator(databasePopulator());
			return initializer;
		}

		private DatabasePopulator databasePopulator() {
			final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
			populator.addScript(schemaScript);
			return populator;
		}
	}
}
