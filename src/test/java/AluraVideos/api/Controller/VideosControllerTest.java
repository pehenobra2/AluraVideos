package AluraVideos.api.Controller;

import AluraVideos.api.DTO.VideosDTO;
import AluraVideos.api.Model.Videos;
import AluraVideos.api.Service.VideosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VideosControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VideosService service;

    private JacksonTester<VideosDTO> videosDTOJson;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informações estão inválidas")
    void testCadastroVideo1() throws Exception{

        var response =mvc.perform(post("/videos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informações estão válidas")
    void testCadastroVideo2() throws Exception{

        var response =mvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(videosDTOJson.write(
                                new VideosDTO("Teste", "testando", "teste.com/video")
                        ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200")
    void testGetAllVideos1() throws Exception{

        var response = mvc.perform(get("/videos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 404 quando não encontra o video")
    void testGetVideo1() throws Exception{

        doThrow(new EntityNotFoundException("Vídeo não encontrado com esse ID")).when(service).getVideo(100L);

        var response = mvc.perform(get("/videos/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 quando encontra o video")
    void testGetVideo2() throws Exception{

        Videos video = new Videos();
        video.setId(100L);
        video.setTitulo("Introdução ao Spring Boot");
        video.setDescricao("Vídeo explicando os conceitos básicos do Spring Boot e como iniciar um projeto.");
        video.setUrl("introducaoSpringBoot.com/video");

        // Configura o mock do serviço para retornar o vídeo de exemplo
        when(service.getVideo(100L)).thenReturn(Optional.of(video));

        var response = mvc.perform(get("/videos/100"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deveria devolver código HTTP 404 quando não encontra o vídeo para deletar")
    void testDeleteVideo1() throws Exception {
        doThrow(new EntityNotFoundException("Vídeo não encontrado com esse ID")).when(service).deleteVideo(100L);

        mvc.perform(delete("/videos/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deveria devolver código HTTP 200 quando encontra o vídeo para deletar")
    void testDeleteVideo2() throws Exception{

        Videos video = new Videos();
        video.setId(100L);
        video.setTitulo("Introdução ao Spring Boot");
        video.setDescricao("Vídeo explicando os conceitos básicos do Spring Boot e como iniciar um projeto.");
        video.setUrl("introducaoSpringBoot.com/video");

        doNothing().when(service).deleteVideo(100L);

        mvc.perform(delete("/videos/100"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deveria devolver código HTTP 404 quando não encontra o vídeo para atualizar")
    void testUpdateVideo1() throws Exception {
        VideosDTO video = new VideosDTO("Teste", "Testando", "teste.com");

        doThrow(new EntityNotFoundException("Vídeo não encontrado com esse ID")).when(service).updateVideo(100L, video);

        mvc.perform(put("/videos/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Teste\", \"descricao\":\"Testando\", \"url\":\"teste.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando consegue atualizar o vídeo")
    void testUpdateVideo2() throws Exception{
        VideosDTO videoDto = new VideosDTO("Teste", "Testando", "teste.com");

        Videos video = new Videos();
        video.setId(100L);
        video.setTitulo("Introdução ao Spring Boot");
        video.setDescricao("Vídeo explicando os conceitos básicos do Spring Boot e como iniciar um projeto.");
        video.setUrl("introducaoSpringBoot.com/video");

        when(service.updateVideo(100L, videoDto)).thenReturn(Optional.of(video));

        var response =mvc.perform(put("/videos/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(videosDTOJson.write(
                                new VideosDTO("Teste", "Testando", "teste.com")
                        ).getJson())
                )
                .andExpect(status().isOk());
    }
}