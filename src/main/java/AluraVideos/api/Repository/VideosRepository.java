package AluraVideos.api.Repository;

import AluraVideos.api.Model.Videos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideosRepository extends JpaRepository<Videos, Long> {

    List<Videos> findAllByOrderByIdAsc();

}
