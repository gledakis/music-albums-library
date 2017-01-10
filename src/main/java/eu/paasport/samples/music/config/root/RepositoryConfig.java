package eu.paasport.samples.music.config.root;

import eu.paasport.samples.music.config.data.RelationalCloudDataSourceConfig;
import eu.paasport.samples.music.config.data.LocalJpaRepositoryConfig;
import eu.paasport.samples.music.repositories.AlbumRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {AlbumRepository.class, RelationalCloudDataSourceConfig.class,})
public class RepositoryConfig {
}

