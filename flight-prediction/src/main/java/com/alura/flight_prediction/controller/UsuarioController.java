package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.DatosRegistroUsuario;
import com.alura.flight_prediction.entity.Usuario;
import com.alura.flight_prediction.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DatosRegistroUsuario datos) {
        try {
            if (usuarioRepository.findByCorreo(datos.correo()) != null) {
                return ResponseEntity.badRequest().body("El usuario ya existe");
            }

            String passwordEncriptada = passwordEncoder.encode(datos.contrasenha());

            Usuario nuevoUsuario = new Usuario(datos,passwordEncriptada);
            usuarioRepository.save(nuevoUsuario);

            return ResponseEntity.ok("Usuario se ha registrado con éxito");
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al registrar usuario");
        }

    }
}