package com.abhiraj.PopcornPing.services;

import com.abhiraj.PopcornPing.domain.entities.User;
import com.abhiraj.PopcornPing.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {

    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new RuntimeException("Unauthenticated Access");
        }

        if(!(authentication.getPrincipal() instanceof UserPrincipal principal)){
            throw  new RuntimeException("Invalid authentication principal");
        }

        return principal.getUser();
    }

}
