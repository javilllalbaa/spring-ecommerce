package com.javier.ecommerce.ecommerce.controllers;

import com.javier.ecommerce.ecommerce.dtos.UserDto;
import com.javier.ecommerce.ecommerce.entities.Producto;
import com.javier.ecommerce.ecommerce.exceptions.AppException;
import com.javier.ecommerce.ecommerce.services.IProductService;
import com.javier.ecommerce.ecommerce.util.SessionValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private SessionValid sessionValid;

    private final Logger logger= LoggerFactory.getLogger(UserController.class);
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(Producto producto, @RequestParam("img") MultipartFile file, @RequestHeader("token") String token, HttpSession session) {
        logger.info("Este es el objeto producto {}",producto);
        int userId = sessionValid.validUser(token, session);
        productService.save(producto, file, userId);
    }


    @PostMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public void update(Producto producto, @RequestParam(value = "img") MultipartFile file, @RequestHeader("token") String token, HttpSession session) {
        logger.info("Actualizar producto", producto);
        int userId = sessionValid.validUser(token, session);
        productService.update(producto, file, userId);
    }

    @GetMapping("")
    @ResponseBody
    public List<Producto> getProducts() {
        try {
            logger.info("Obtener los productos {}");
            return productService.findAll();
        }catch (Exception ex) {
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listUser")
    @ResponseBody
    public List<Producto> getProductsUser(@RequestHeader("token") String token, HttpSession session) {
        try {
            logger.info("Obtener los productos creados por un usuario {}");
            int userId = sessionValid.validUser(token, session);
            return productService.listProductsUsers(userId);
        }catch (Exception ex) {
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{idProduct}")
    public void DeleteProductsUser(@PathVariable int idProduct, @RequestHeader("token") String token, HttpSession session) {
        try {
            logger.info("Eliminar producto creado por un usuario {}", idProduct);
            sessionValid.validUser(token, session);
            productService.DeleteProduct(idProduct);
        }catch (Exception ex) {
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public List<Producto> searchProduct(@RequestParam String nombre) {
        logger.info("Buscar Productos {}", nombre);
        return productService.listSearch(nombre);
    }



}
