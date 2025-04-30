package com.frogcrew.frogcrew.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class InviteRequestDTO {
    @NotEmpty(message = "Email list cannot be empty")
    private List<@Email(message = "Invalid email format") String> emails;
}