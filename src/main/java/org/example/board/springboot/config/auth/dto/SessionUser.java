package org.example.board.springboot.config.auth.dto;

import lombok.Getter;
import org.example.board.springboot.domain.user.User;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
