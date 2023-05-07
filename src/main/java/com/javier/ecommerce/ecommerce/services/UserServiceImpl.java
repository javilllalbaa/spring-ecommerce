package com.javier.ecommerce.ecommerce.services;

import com.javier.ecommerce.ecommerce.controllers.UserController;
import com.javier.ecommerce.ecommerce.entities.Usuario;
import com.javier.ecommerce.ecommerce.exceptions.AppException;
import com.javier.ecommerce.ecommerce.repositories.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {
    private final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUsuarioRepository usuarioRepository;
    BCryptPasswordEncoder encrypt= new BCryptPasswordEncoder();

    @Override
    public Usuario save(Usuario usuario) {
        try{
            Optional<Usuario> userFind = usuarioRepository.findByEmail(usuario.getEmail());
            if (!userFind.isPresent()) {
                usuario.setTipo("USER");
                usuario.setPassword( encrypt.encode(usuario.getPassword()));
            }else{
                throw new AppException("User account already exists", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            throw new AppException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return usuarioRepository.save(usuario);

    }

    @Override
    public String singIn(Usuario usuario, HttpSession session) {
        logger.info("Usuario de db: {}", usuario);
        Optional<Usuario> userFind = usuarioRepository.findByEmail(usuario.getEmail());
        if (userFind.isPresent()) {
            if(encrypt.matches(usuario.getPassword(), userFind.get().getPassword())){
                String uuid = UUID.randomUUID().toString();
                logger.info("Usuario uuid: {}", uuid);
                session.setAttribute(uuid, userFind.get().getId());
                return uuid;
            }else{
                throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
            }
        }else {
            throw new AppException("Unknown user", HttpStatus.NOT_FOUND);
        }

    }


}