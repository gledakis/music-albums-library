package eu.paasport.samples.music.repositories.jpa;

import eu.paasport.samples.music.domain.Album;
import eu.paasport.samples.music.repositories.AlbumRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAlbumRepository extends JpaRepository<Album, String>, AlbumRepository {
}
