package com.example.contactmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactCreateDTO {

    @Schema(example = "John Doe", description = "The full name of the contact")
    @NotBlank(message = "Name must not be empty")
    private String name;

    @Schema(example = "test@example.com", description = "The email address of the contact")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(example = "380-XXX-XXX-XXX", description = "The phone number of the contact")
    @NotBlank(message = "Phone must not be empty")
    private String phone;
}
