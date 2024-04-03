package com.unevento.api.controllers;

import com.unevento.api.domain.modelo.Eventos;
import com.unevento.api.domain.modelo.Usuario;
import com.unevento.api.domain.records.NewEvent;
import com.unevento.api.domain.records.UpdateAnswerDataEvent;
import com.unevento.api.domain.repository.EventRepository;
import com.unevento.api.domain.repository.UserRepository;
import com.unevento.api.services.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@CrossOrigin
@RequestMapping("/newevent")

public class NewEvents {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final com.unevento.api.services.FileUploadService fileUploadService;

    public NewEvents(EventRepository eventRepository, UserRepository userRepository, FileUploadService fileUploadService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fileUploadService = fileUploadService;
    }


    @PostMapping
    public ResponseEntity<UpdateAnswerDataEvent> creatingEvent(@RequestPart("newEvent") NewEvent newEvent, UriComponentsBuilder uriBuilder, @RequestPart(value = "file", required = false) MultipartFile file) {
        Usuario user = userRepository.getById(newEvent.userID());
        Eventos eventos;
        try {
            String imagePath;
            if (file == null || file.isEmpty()) {
                // If no file is provided or the file is empty, assign a default image path
                imagePath = "Backend/src/main/resources/images/as.png"; // Replace with your default image path
            } else {
                // Use FileUploadService to handle file upload and get the path
                imagePath = fileUploadService.uploadFile(file);
            }

            eventos = eventRepository.save(new Eventos(newEvent, user, imagePath));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        UpdateAnswerDataEvent answer = new UpdateAnswerDataEvent(eventos.getIdevento(), eventos.getNombre(), eventos.getDescripcion(), eventos.getLugar(), eventos.getCategoria(), eventos.getFacultad(), eventos.getFecha_evento(), eventos.getCapacidad(), eventos.getHora());
        URI uri = uriBuilder.path("/getevent/{id}").buildAndExpand(eventos.getIdevento()).toUri();
        return ResponseEntity.created(uri).body(answer);
    }
}
