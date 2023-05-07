package com.javier.ecommerce.ecommerce.repositories;

import java.util.List;

import com.javier.ecommerce.ecommerce.entities.Orden;
import com.javier.ecommerce.ecommerce.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {
    List<Orden> findByUsuario (Usuario usuario);
}
