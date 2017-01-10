package eu.paasport.samples.music.config.data;

import eu.paasport.portability.Cloud;
import eu.paasport.portability.CloudFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.dialect.H2Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@Profile("in-memory")
@EnableJpaRepositories("eu.paasport.samples.music.repositories.jpa")
public class LocalJpaRepositoryConfig extends AbstractLocalDataSourceConfig {

        CloudFactory cloudFactory = new CloudFactory();

    @Bean
    public DataSource dataSource() {
            System.out.println(":::LocalJPA Called");

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(LocalJpaRepositoryConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
//
//        Cloud cloud = cloudFactory.getCloud();
//        Properties cloudProperties = cloud.getCloudProperties();
//
//        String username = cloudProperties.getProperty("cloud.services.mysql.connection.username");
//        if(username == null || "null".equals(username)){
//        username = cloudProperties.getProperty("cloud.application.connection.username");
//        }
//        String password = cloudProperties.getProperty("cloud.services.mysql.connection.password");
//        if(password == null || "null".equals(password)){
//        password = cloudProperties.getProperty("cloud.application.connection.password");
//        }
//        String url = cloudProperties.getProperty("cloud.services.mysql.connection.jdbcurl");
//        if(url == null || "null".equals(url)){
//        url = cloudProperties.getProperty("cloud.application.connection.url");
//        }
//        String host = cloudProperties.getProperty("cloud.services.mysql.connection.host");
//        if(host == null || "null".equals(host)){
//        host = cloudProperties.getProperty("cloud.application.connection.host");
//        }
//        String port = cloudProperties.getProperty("cloud.services.mysql.connection.port");
//        if(port == null || "null".equals(host)){
//        port = cloudProperties.getProperty("cloud.application.connection.port");
//        }    
//            String url1 = "jdbc:mysql://localhost:3306/paasport";
//            String user = "paasport";
//            String apassword = "!paasport!";
//                System.out.println("Connected to the database connection/datasourse");
//            
//            BasicDataSource dataSource = new BasicDataSource();
//            
//            dataSource.setUrl(url1);
//            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//            dataSource.setUsername(user);
//            dataSource.setPassword(apassword);
//            return  dataSource;
                //mysql://$OPENSHIFT_MYSQL_DB_HOST:$OPENSHIFT_MYSQL_DB_PORT/
                String mssqlhost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");//The host name or IP address used to connect to the database.
                String mssqlport = System.getenv("OPENSHIFT_MYSQL_DB_PORT");//The port the database server is listening on.
                String username = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");//The database administrative user name.
                String password = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");//The database administrative user’s password.
                String url = System.getenv("OPENSHIFT_MYSQL_DB_URL");//Database connection URL.
                String name = System.getenv("OPENSHIFT_GEAR_NAME");//Database name, should be the same as gear name.
                String connect = "jdbc:mysql://" + mssqlhost + ":" + mssqlport + "/" + name;
                
                
//String url1 = "jdbc:mysql://localhost:3306/paasport";
                //String user = "paasport";
                //String password = "!paasport!";
                //Class.forName("com.mysql.jdbc.Driver");
                BasicDataSource dataSource = new BasicDataSource();
                dataSource.setUrl(url);
                dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                dataSource.setUsername(username);
                dataSource.setPassword(password);
            
                //String msg="not connected";
                //Connection connection = DriverManager.getConnection(connect, username, password);
                System.out.println(":::LocalJPA Called, url:::"+url);

                
                return createBasicDataSource(connect, "com.mysql.jdbc.Driver", username, password);
                //return createBasicDataSource("jdbc:mysql:127.7.72.130:3306/paasportmusic", "com.mysql.jdbc.Driver", "adminuTTNmrR", "JPB-_S_JU419");

    }

  
    protected BasicDataSource createBasicDataSource(String jdbcUrl, String driverClass, String userName, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
            try {
                System.out.println(":::LocalJPA Called, connection created:::"+dataSource.getConnection().toString());
            } catch (SQLException ex) {
                Logger.getLogger(LocalJpaRepositoryConfig.class.getName()).log(Level.SEVERE, null, ex);
            }

        
        
        return dataSource;
    }

    @Override
    protected String getHibernateDialect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
