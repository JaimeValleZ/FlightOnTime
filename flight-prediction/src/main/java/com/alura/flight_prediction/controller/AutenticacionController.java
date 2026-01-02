package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.DatosAutenticacion;
import com.alura.flight_prediction.dto.DatosTokenJWT;
import com.alura.flight_prediction.entity.Usuario;
import com.alura.flight_prediction.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAutenticacion datosAutenticacion) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(datosAutenticacion.correo(),
                datosAutenticacion.contrasenha());

        // Aquí es donde Spring valida la contraseña contra la base de datos (BCrypt)
        var usuarioAutenticado = authenticationManager.authenticate(authToken);

        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());

        return ResponseEntity.ok(new DatosTokenJWT(JWTtoken));
    }
}