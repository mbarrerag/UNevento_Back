package com.unevento.api.controllers;

import com.unevento.api.controllers.services.BadWordsHandler.ContentFilterService;
import com.unevento.api.controllers.services.FileService;
import com.unevento.api.domain.modelo.Categorias;
import com.unevento.api.domain.modelo.Eventos;
import com.unevento.api.domain.modelo.Facultades;
import com.unevento.api.domain.records.UpdateAnswerDataEvent;
import com.unevento.api.domain.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@Transactional
@RequestMapping("/updatetevent")
public class UpdateEvent {

    private final EventRepository eventRepository;

    private final FileService fileService;

    private final ContentFilterService contentFilterService;

    public UpdateEvent(EventRepository eventRepository, FileService fileService, ContentFilterService contentFilterService) {
        this.eventRepository = eventRepository;
        this.fileService = fileService;
        this.contentFilterService = contentFilterService;
    }

    @Transactional
    @CrossOrigin
    @PutMapping
    public ResponseEntity<UpdateAnswerDataEvent> updateEvent(@RequestPart("UpdateEvent") com.unevento.api.domain.records.UpdateEvent updateEvent, @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Eventos eventos = eventRepository.getReferenceById(updateEvent.id());
            eventos.setNombre(updateEvent.nombre());
            eventos.setDescripcion(updateEvent.descripcion());
            eventos.setFecha_evento(updateEvent.fechaEvento());
            eventos.setHora(updateEvent.hora());
            eventos.setLugar(updateEvent.lugar());
            eventos.setCapacidad(updateEvent.capacidad());
            eventos.setFacultad(Facultades.valueOf(updateEvent.Facultad()));
            eventos.setCategoria(Categorias.valueOf(updateEvent.categoria()));

            String imageUrl = null;

            if (file != null && !file.isEmpty()) {

                imageUrl = fileService.upload(file);
            } else {
                // If no new file is provided, keep the existing image path
                imageUrl = eventos.getImagen_path();
            }

            eventos.setImagen_path(imageUrl);

            if (contentFilterService.containsBadWords(eventos.getNombre()) || contentFilterService.containsBadWords(eventos.getDescripcion()) || contentFilterService.containsBadWords(eventos.getLugar())) {
                return ResponseEntity.badRequest().body(null);
            }
            // Guardar la entidad actualizada en la base de datos
            eventRepository.save(eventos);

            return ResponseEntity.ok(new UpdateAnswerDataEvent(eventos.getIdevento(), eventos.getNombre(), eventos.getDescripcion(), eventos.getLugar(), eventos.getCategoria(), eventos.getFacultad(), eventos.getFecha_evento(), eventos.getCapacidad(), eventos.getHora(), eventos.getImagen_path(), eventos.getTipo()));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
