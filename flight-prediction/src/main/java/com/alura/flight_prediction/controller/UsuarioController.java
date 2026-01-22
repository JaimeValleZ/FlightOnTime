package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.user.DatosRegistroUsuario;
import com.alura.flight_prediction.dto.user.UsuarioInfoDTO;
import com.alura.flight_prediction.entity.Usuario;
import com.alura.flight_prediction.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DatosRegistroUsuario datos) {

        if (usuarioRepository.findByCorreo(datos.correo()) != null) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "El usuario ya existe")
            );
        }

        String passwordEncriptada = passwordEncoder.encode(datos.contrasenha());

        Usuario nuevoUsuario = new Usuario(datos, passwordEncriptada);
        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok(
                Map.of("message", "Usuario se ha registrado con éxito")
        );
    }

    @GetMapping("/me")
    public UsuarioInfoDTO me(
            @AuthenticationPrincipal Usuario usuario) {

        return new UsuarioInfoDTO(
                usuario.getNombre(),
                usuario.getCorreo()
        );
    }

}