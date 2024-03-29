package com.unevento.api.controllers;

import com.unevento.api.domain.modelo.Usuario;
import com.unevento.api.domain.records.UpdateAnswerDataUser;
import com.unevento.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updateuser")
public class UpdateUser {

    public final UserRepository userRepository;

    public UpdateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    @PutMapping
    public ResponseEntity<UpdateAnswerDataUser> updateUser(@RequestBody com.unevento.api.domain.records.UpdateUser dataUser) {
        try {
            Usuario usuario = userRepository.getById(dataUser.id());
            // Actualizar los datos del usuario con los valores proporcionados en dataUser
            usuario.setNombre(dataUser.nombre());
            usuario.setApellido(dataUser.apellido());
            usuario.setCorreo(dataUser.correo());
            usuario.setPassword(dataUser.contrasena());

            // Guardar la entidad actualizada en la base de datos
            userRepository.save(usuario);

            return ResponseEntity.ok(new UpdateAnswerDataUser(usuario.getIdUsuario(), usuario.getNombre(), usuario.getApellido(), usuario.getCorreo(), usuario.getPassword()));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}

