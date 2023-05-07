package com.javier.ecommerce.ecommerce.services;

import com.javier.ecommerce.ecommerce.entities.Usuario;

import javax.servlet.http.HttpSession;
import java.util.Optional;


public interface IUserService {
    Usuario save (Usuario usuario);

    String singIn(Usuario usuario, HttpSession session);

}
