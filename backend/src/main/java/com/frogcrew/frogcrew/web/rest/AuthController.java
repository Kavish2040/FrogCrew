package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.security.JwtTokenProvider;
import com.frogcrew.frogcrew.service.UserService;
import com.frogcrew.frogcrew.service.dto.AuthDTO;
import com.frogcrew.frogcrew.service.dto.LoginQueryDTO;
import com.frogcrew.frogcrew.service.dto.PasswordResetDTO;
import com.frogcrew.frogcrew.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider jwtTokenProvider;
        private final UserService userService;

        @PostMapping("/auth/login")
        public ResponseEntity<ApiResponse<AuthDTO>> login(
                        @ModelAttribute LoginQueryDTO loginQuery) {
                try {
                        // Authenticate with username and password
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(loginQuery.getEmail(),
                                                        loginQuery.getPassword()));

                        // Generate JWT token
                        String token = jwtTokenProvider.generateAccessToken(authentication);

                        // Get user role
                        String role = authentication.getAuthorities().stream()
                                        .findFirst()
                                        .map(GrantedAuthority::getAuthority)
                                        .orElse("ROLE_USER")
                                        .replace("ROLE_", "");

                        // Get user info by username (email)
                        List<UserDTO> users = userService.listAll();
                        UserDTO user = users.stream()
                                        .filter(u -> u.getEmail().equals(loginQuery.getEmail()))
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        // Return token, role, and user ID in AuthDTO object
                        AuthDTO authDTO = new AuthDTO(user.getId(), role, token);

                        return ResponseEntity.ok(ApiResponse.success("Login Success", authDTO));
                } catch (BadCredentialsException e) {
                        return ResponseEntity.ok(ApiResponse.unauthorized("username or password is incorrect"));
                }
        }

        @PostMapping("/auth/resetPassword")
        public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody PasswordResetDTO request) {
                try {
                        userService.resetPassword(request.getToken(), request.getNewPassword());
                        return ResponseEntity.ok(ApiResponse.success("Password reset successfully", null));
                } catch (Exception e) {
                        if (e.getMessage() != null) {
                                if (e.getMessage().contains("Invalid reset password token")) {
                                        return ResponseEntity.ok(ApiResponse.badRequest(
                                                        "Invalid reset password token for email: " + request.getEmail(),
                                                        null));
                                } else if (e.getMessage().contains("Reset password token has expired")) {
                                        return ResponseEntity.ok(ApiResponse.badRequest(
                                                        "Reset password token has expired for email: "
                                                                        + request.getEmail(),
                                                        null));
                                } else if (e.getMessage().contains("Password policy")) {
                                        return ResponseEntity.ok(ApiResponse.badRequest(
                                                        "Password must contain at least one letter and one number, and at least 6 characters.",
                                                        null));
                                }
                        }
                        return ResponseEntity.ok(ApiResponse.badRequest("Password reset failed", null));
                }
        }

        @PostMapping("/auth/forgotPassword/{email}")
        public ResponseEntity<ApiResponse<Void>> forgotPassword(@PathVariable String email) {
                try {
                        userService.sendResetPasswordLink(email);
                        return ResponseEntity.ok(ApiResponse.success("Password reset email sent successfully", null));
                } catch (Exception e) {
                        return ResponseEntity.ok(ApiResponse
                                        .notFound("Could not find email with this property: " + email + " :("));
                }
        }
}