package eu.paasport.samples.music.config.data;

import eu.paasport.portability.Cloud;
import eu.paasport.portability.CloudFactory;

import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import eu.paasport.samples.music.repositories.AlbumRepository;
import org.hibernate.dialect.MySQL5Dialect;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@Profile({"mysql-cloud", "postgres-cloud", "oracle-cloud"})
@ComponentScan(basePackageClasses = {AlbumRepository.class, RelationalCloudDataSourceConfig.class,})
public class RelationalCloudDataSourceConfig extends AbstractLocalDataSourceConfig {
                        
            CloudFactory cloudFactory = new CloudFactory();
            

    
    @Bean
    public DataSource dataSource() {
        /*
        Cloud cloud = cloudFactory.getCloud();    
        Properties cloudProperties = cloud.getCloudProperties();

        String username = cloudProperties.getProperty("cloud.services.mysql.connection.username");
        if(username == null || "null".equals(username)){
        username = cloudProperties.getProperty("cloud.application.connection.username");
        }
        String password = cloudProperties.getProperty("cloud.services.mysql.connection.password");
        if(password == null || "null".equals(password)){
        password = cloudProperties.getProperty("cloud.application.connection.password");
        }
        String url = cloudProperties.getProperty("cloud.services.mysql.connection.jdbcurl");
        if(url == null || "null".equals(url)){
        url = cloudProperties.getProperty("cloud.application.connection.url");
        }
        String host = cloudProperties.getProperty("cloud.services.mysql.connection.host");
        if(host == null || "null".equals(host)){
        host = cloudProperties.getProperty("cloud.application.connection.host");
        }
        String port = cloudProperties.getProperty("cloud.services.mysql.connection.port");
        if(port == null || "null".equals(host)){
        port = cloudProperties.getProperty("cloud.application.connection.port");
        }
        
        ///Fix this properly
        String name = cloudProperties.getProperty("cloud.services.mysql.connection.port");
        if(name == null || "null".equals(name)){
        name = cloudProperties.getProperty("cloud.application.name");
            System.out.println("echoing name:::"+name);
            name = System.getenv("OPENSHIFT_GEAR_NAME");//Database name, should be the same as gear name.
        }else{
            System.out.println("echoing url:::"+url);
            name =dbNamefromUrl(url);
            System.out.println("echoing name:::"+name);

        }
    
        */

        
        Properties portableinfo = cloudFactory.getCloud().getCloudProperties();

        String url = portableinfo.getProperty("cloud.services.mysql.connection.jdbcurl");
        String host = portableinfo.getProperty("cloud.services.mysql.connection.host");
        String port = portableinfo.getProperty("cloud.services.mysql.connection.port");
        String username = portableinfo.getProperty("cloud.services.mysql.connection.username");
        String password = portableinfo.getProperty("cloud.services.mysql.connection.password");
        String name = portableinfo.getProperty("cloud.services.mysql.connection.port");

        //tmp fix to recent updates
        if (name == null || "null".equals(name)) {
            name = portableinfo.getProperty("cloud.application.name");
            name = System.getenv("OPENSHIFT_GEAR_NAME");//Database name, should be the same as gear name.
        } else {
            name = dbNamefromUrl(url);
        }
        


        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocalJpaRepositoryConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        String connect = "jdbc:mysql://" + host + ":" + port + "/" + name;

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(connect);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        try {
            System.out.println(":::RelationalCloud Called, connection created:::" + dataSource.getConnection().toString());
        } catch (SQLException ex) {
            Logger.getLogger(LocalJpaRepositoryConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataSource;

}

    @Override
    protected String getHibernateDialect() {
        return MySQL5Dialect.class.getName();
    }

    
        private String dbNamefromUrl(String dburl){
        String dbname;
        String[] parts;
        parts = dburl.split("\\?reconnect");
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // true ->dissmiss
        String[] partsV2 = part1.split("3306/");
        dbname = partsV2[1].trim();
        return dbname;
    }
    
    
}
