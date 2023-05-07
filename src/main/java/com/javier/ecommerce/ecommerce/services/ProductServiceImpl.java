package com.javier.ecommerce.ecommerce.services;

import com.javier.ecommerce.ecommerce.controllers.UserController;
import com.javier.ecommerce.ecommerce.entities.Producto;
import com.javier.ecommerce.ecommerce.entities.Usuario;
import com.javier.ecommerce.ecommerce.exceptions.AppException;
import com.javier.ecommerce.ecommerce.repositories.IProductoRepository;
import com.javier.ecommerce.ecommerce.repositories.IUsuarioRepository;
import com.javier.ecommerce.ecommerce.util.UploadFile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements IProductService {

    private final Logger logger= LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private UploadFile uploadFile;

    @Override
    public Producto save(Producto producto, MultipartFile file, int userId) {
        try {
            logger.info("Creando producto... {}",producto);
            Usuario u= usuarioRepository.findById(userId).get();
            producto.setUsuario(u);

            if (producto.getId()==null) { // cuando se crea un producto
                String nombreImagen= uploadFile.saveImage(file);
                producto.setImagen(nombreImagen);

            }
            productoRepository.save(producto);
            return producto;
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public Producto update(Producto producto, MultipartFile file, int userId) {
        try {
            logger.info("Creando producto... {}",producto);
            Producto p = new Producto();
            if(producto.getId() == null ) {
                throw new AppException("Unknown product", HttpStatus.BAD_REQUEST);
            }
            p = this.get(producto.getId()).get();

            // Se actualiza el producto pero no la imagen
            if (file.isEmpty()) {
                producto.setImagen(p.getImagen());
            }else {
                // Se actualiza la imagen si es diferente a la por defecto
                if (!p.getImagen().equals("default.jpg")) {
                    uploadFile.deleteImage(p.getImagen());
                }
                String nombreImagen= uploadFile.saveImage(file);
                producto.setImagen(nombreImagen);
            }
            producto.setUsuario(p.getUsuario());
            this.update(producto);
            return producto;
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public Optional<Producto> get(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public void update(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public List<Producto> findAll() {
        try {
            return productoRepository.findAll();
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public List<Producto> listProductsUsers(int userId) {
        try {
            return productoRepository.findAll().stream().filter(p -> p.getUsuario().getId() == userId).toList();
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public void DeleteProduct(int idProduct) {
        try {
            Producto p = new Producto();
            p = this.get(idProduct).get();

            //eliminar cuando no sea la imagen por defecto
            if (!p.getImagen().equals("default.jpg")) {
                uploadFile.deleteImage(p.getImagen());
            }
            productoRepository.deleteById(idProduct);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public List<Producto> listSearch(String nombre) {
        try {
            return productoRepository.findAll().stream().filter(p -> p.getNombre().contains(nombre)).toList();
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
