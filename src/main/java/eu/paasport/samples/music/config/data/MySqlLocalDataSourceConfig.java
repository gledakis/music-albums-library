package eu.paasport.samples.music.config.data;

import eu.paasport.portability.Cloud;
import eu.paasport.portability.CloudFactory;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

@Configuration
@Profile("mysql-local")
public class MySqlLocalDataSourceConfig extends AbstractLocalDataSourceConfig {
        CloudFactory cloudFactory = new CloudFactory();

    @Bean
    public DataSource dataSource() {
        
        
            System.out.println(":::MysqlLocal Called");

                String mssqlhost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");//The host name or IP address used to connect to the database.
                String mssqlport = System.getenv("OPENSHIFT_MYSQL_DB_PORT");//The port the database server is listening on.
                String username = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");//The database administrative user name.
                String password = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");//The database administrative user’s password.
                String url = System.getenv("OPENSHIFT_MYSQL_DB_URL");//Database connection URL.
                String name = System.getenv("OPENSHIFT_GEAR_NAME");//Database name, should be the same as gear name.
                String connect = "jdbc:mysql://" + mssqlhost + ":" + mssqlport + "/" + name;
                
                BasicDataSource dataSource = new BasicDataSource();
                dataSource.setUrl(url);
                dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                dataSource.setUsername(username);
                dataSource.setPassword(password);

                return createBasicDataSource(connect, "com.mysql.jdbc.Driver", username, password);
    }

    @Override
    protected String getHibernateDialect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
