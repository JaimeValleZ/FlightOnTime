package com.alura.flight_prediction.repository;

import com.alura.flight_prediction.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByCorreo(String email);
    Usuario findByNombre(String nombre);
}
