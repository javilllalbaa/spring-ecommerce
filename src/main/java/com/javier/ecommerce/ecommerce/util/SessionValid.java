package com.javier.ecommerce.ecommerce.util;

import com.javier.ecommerce.ecommerce.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SessionValid {

    public int validUser(String token, HttpSession session) {
        try {
            return Integer.parseInt(session.getAttribute(token).toString());
        } catch (Exception ex) {
            throw new AppException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}
