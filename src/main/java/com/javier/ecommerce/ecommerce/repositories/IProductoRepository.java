package com.javier.ecommerce.ecommerce.repositories;

import com.javier.ecommerce.ecommerce.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> {

}
