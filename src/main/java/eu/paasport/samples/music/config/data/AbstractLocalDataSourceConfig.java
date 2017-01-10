package eu.paasport.samples.music.config.data;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import eu.paasport.samples.music.domain.Album;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;


@EnableJpaRepositories("eu.paasport.samples.music.repositories.jpa")
public abstract class AbstractLocalDataSourceConfig {

    
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        return createEntityManagerFactoryBean(dataSource, getHibernateDialect());
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    protected abstract String getHibernateDialect();

    protected LocalContainerEntityManagerFactoryBean createEntityManagerFactoryBean(DataSource dataSource, String dialectClassName) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "update");
        properties.put(org.hibernate.cfg.Environment.DIALECT, dialectClassName);
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");
        System.out.println("Hibernate properties:::");
        System.out.println("Hibernate properties, datasource:::"+dataSource.toString());
        System.out.println("Hibernate properties, class:::"+dialectClassName);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(Album.class.getPackage().getName());
        em.setPersistenceProvider(new HibernatePersistence());
        em.setJpaPropertyMap(properties);
        return em;
    }
    
    protected BasicDataSource createBasicDataSource(String jdbcUrl, String driverClass, String userName, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }
}
