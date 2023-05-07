package com.javier.ecommerce.ecommerce.services;

import com.javier.ecommerce.ecommerce.entities.Producto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface IProductService {

    public Producto save(Producto producto, MultipartFile file, int userId);
    public Producto update(Producto producto, MultipartFile file, int userId);
    Optional<Producto> get(Integer id);
    public void update(Producto producto);
    public List<Producto> findAll();
    public List<Producto> listProductsUsers(int userId);
    public void DeleteProduct(int idProduct);
    public List<Producto> listSearch(String nombre);

}
