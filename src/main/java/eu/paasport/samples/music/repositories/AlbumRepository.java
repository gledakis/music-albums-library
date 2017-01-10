package eu.paasport.samples.music.repositories;

import eu.paasport.samples.music.domain.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, String> {
}
