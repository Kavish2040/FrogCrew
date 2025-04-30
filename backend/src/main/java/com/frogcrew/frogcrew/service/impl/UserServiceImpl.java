package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import com.frogcrew.frogcrew.repository.FrogCrewUserRepository;
import com.frogcrew.frogcrew.service.MailService;
import com.frogcrew.frogcrew.service.UserService;
import com.frogcrew.frogcrew.service.dto.UserDTO;
import com.frogcrew.frogcrew.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final FrogCrewUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        FrogCrewUser user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        FrogCrewUser existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        FrogCrewUser updatedUser = userMapper.toEntity(userDTO);
        updatedUser.setId(existingUser.getId());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            updatedUser.setPassword(existingUser.getPassword());
        }
        updatedUser = userRepository.save(updatedUser);

        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> listAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void disableUser(UUID id) {
        FrogCrewUser user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // Soft delete by setting active status to false
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void sendResetPasswordLink(String email) {
        FrogCrewUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        // Generate password reset token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(ZonedDateTime.now().plusHours(24));
        userRepository.save(user);

        // Send email with reset link
        mailService.sendEmail(
                user.getEmail(),
                "Password Reset Request",
                "To reset your password, click the link below:\n\n" +
                        "http://localhost:8080/reset-password?token=" + token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        FrogCrewUser user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invalid password reset token"));

        if (user.getResetTokenExpiry().isBefore(ZonedDateTime.now())) {
            throw new IllegalStateException("Password reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
}