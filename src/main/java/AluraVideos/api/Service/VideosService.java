package AluraVideos.api.Service;

import AluraVideos.api.DTO.VideosDTO;
import AluraVideos.api.Model.Videos;
import AluraVideos.api.Repository.VideosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideosService {

    @Autowired
    private VideosRepository repository;



    public Videos cadastroVideo(VideosDTO videosDTO){
        Videos video = new Videos();
        video.setDescricao(videosDTO.descricao());
        video.setUrl(videosDTO.url());
        video.setTitulo(videosDTO.titulo());

        return repository.save(video);
    }

    public Optional<List<Videos>> getAllVideos(){
        List<Videos> videos = repository.findAllByOrderByIdAsc();
        if(videos.isEmpty()){
            throw new EntityNotFoundException("Nenhum vídeo cadastrado!");
        }
        return videos.isEmpty() ? Optional.empty() : Optional.of(videos);
    }

    public Optional<Videos> getVideo(Long id){

        Optional<Videos> video = repository.findById(id);
        if(video.isEmpty()){
            throw new EntityNotFoundException("Vídeo não encontrado com esse IP");
        }
        return video;
    }

    public void deleteVideo(Long id){
        Optional<Videos> video = repository.findById(id);
        if(video.isEmpty()){
            throw new EntityNotFoundException("Vídeo não encontrado com esse IP");
        }
        repository.delete(video.get());
    }

    public Optional<Videos> updateVideo(Long id, VideosDTO videosDTO ){
        Optional<Videos> video = repository.findById(id);
        if(video.isEmpty()){
            throw new EntityNotFoundException("Vídeo não encontrado com esse IP");
        }

        Videos videoAtt = video.get();
        videoAtt.setTitulo(videosDTO.titulo());
        videoAtt.setDescricao(videosDTO.descricao());
        videoAtt.setUrl(videosDTO.url());
        return Optional.of(repository.save(videoAtt));

    }


}
