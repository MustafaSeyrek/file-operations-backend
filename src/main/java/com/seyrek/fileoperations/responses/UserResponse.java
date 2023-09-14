package com.seyrek.fileoperations.responses;

import com.seyrek.fileoperations.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
public class UserResponse {

    Long id;
    String username;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
    }
}
