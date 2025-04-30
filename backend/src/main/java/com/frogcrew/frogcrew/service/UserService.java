package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.service.dto.UserDTO;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(UUID id, UserDTO userDTO);

    List<UserDTO> listAll();

    UserDTO findById(UUID id);

    void deleteUser(UUID id);

    void disableUser(UUID id);

    void resetPassword(String token, String newPassword);

    void sendResetPasswordLink(String email);
}