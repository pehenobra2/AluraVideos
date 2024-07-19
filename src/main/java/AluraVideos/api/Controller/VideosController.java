package AluraVideos.api.Controller;

import AluraVideos.api.DTO.VideosDTO;
import AluraVideos.api.Model.Videos;
import AluraVideos.api.Service.VideosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
public class VideosController {

    @Autowired
    private VideosService service;

    @PostMapping
    @Transactional
    public Videos cadastroVideo(@RequestBody @Valid VideosDTO video){
        Videos videoCadastrado = service.cadastroVideo(video);

        return videoCadastrado;
    }

    @GetMapping
    public Optional<List<Videos>> getAllVideos(){
        Optional<List<Videos>> videos = service.getAllVideos();
        return videos;
    }

    @GetMapping("/{id}")
    public Optional<Videos> getVideo(@PathVariable Long id){
        Optional<Videos> video = service.getVideo(id);
        return video;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public String deleteVideo(@PathVariable Long id){
        service.deleteVideo(id);
        return "Vídeo deletado!";
    }

    @PutMapping("/{id}")
    @Transactional
    public Optional<Videos> updateVideo(@PathVariable Long id,@RequestBody @Valid VideosDTO videosDTO){
        Optional<Videos> videoAtt = service.updateVideo(id, videosDTO);
        return videoAtt;
    }


}
