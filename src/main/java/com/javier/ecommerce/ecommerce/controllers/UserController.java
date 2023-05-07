package com.javier.ecommerce.ecommerce.controllers;

import com.javier.ecommerce.ecommerce.dtos.UserDto;
import com.javier.ecommerce.ecommerce.entities.Usuario;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.javier.ecommerce.ecommerce.services.IUserService;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Usuario usuario) {
        logger.info("Usuario registro: {}", usuario);
        userService.save(usuario);
    }

    @PostMapping(value = "/singIn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<UserDto> singIn(@RequestBody Usuario usuario, HttpSession session) {
        logger.info("singIn : {}", usuario);
        UserDto userDto = new UserDto();
        userDto.setToken(userService.singIn(usuario, session));
        return ResponseEntity.ok(userDto);
    }


}
