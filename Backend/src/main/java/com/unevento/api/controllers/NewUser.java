package com.unevento.api.controllers;

import com.unevento.api.controllers.services.BadWordsHandler.ContentFilterService;
import com.unevento.api.domain.modelo.Usuario;
import com.unevento.api.domain.records.UpdateAnswerDataUser;
import com.unevento.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@CrossOrigin
@RequestMapping("/newuser")
public class NewUser {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ContentFilterService contentFilterService;


    public NewUser(UserRepository userRepository, PasswordEncoder passwordEncoder, ContentFilterService contentFilterService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.contentFilterService = contentFilterService;
    }

    @PostMapping
    public ResponseEntity<UpdateAnswerDataUser> newUser(@RequestBody com.unevento.api.domain.records.NewUser dataUser, UriComponentsBuilder uriBuilder) {
        try {


            String encodedPassword = passwordEncoder.encode(dataUser.contrasena());
            Usuario user = new Usuario(dataUser);
            user.setPassword(encodedPassword);
            user.setImagen_path("UserPhoto.jpg");
            if (contentFilterService.containsBadWords(user.getNombre()) || contentFilterService.containsBadWords(user.getUsername())) {
                return ResponseEntity.badRequest().body(null);
            }
            user = userRepository.save(user);
            UpdateAnswerDataUser answer = new UpdateAnswerDataUser(user.getIdUsuario(), user.getCorreo(), user.getNombre(), user.getApellido(), user.getImagen_path());
            URI uri = uriBuilder.path("/getuser/{id}").buildAndExpand(user.getIdUsuario()).toUri();
            return ResponseEntity.created(uri).body(answer);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }


    }
}