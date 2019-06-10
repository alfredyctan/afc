package org.afc.spring.jpa;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@EnableConfigurationProperties(JpaProperties.class)
@Configuration
public class DefaultHibernateJpaConfiguration {

	private final JpaProperties properties;

	public DefaultHibernateJpaConfiguration(JpaProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConfigurationProperties("spring.jpa")
	public JpaVendorAdapter jpaVendorAdapter() {
		AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(this.properties.isShowSql());
		adapter.setDatabasePlatform(this.properties.getDatabasePlatform());
		adapter.setGenerateDdl(this.properties.isGenerateDdl());
		return adapter;
	}

	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter, ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
		EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, this.properties.getProperties(), null);
		return builder;
	}
}
