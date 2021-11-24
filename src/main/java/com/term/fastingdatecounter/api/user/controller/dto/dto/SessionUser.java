package com.term.fastingdatecounter.api.user.controller.dto.dto;

import com.term.fastingdatecounter.api.user.domain.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * Save authenticated user information.
 */

@Getter
public class SessionUser implements Serializable {
    private Long id;
    private String name;
    private String email;

    public SessionUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
