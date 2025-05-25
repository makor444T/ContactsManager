package com.example.contactmanager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1", description = "Unique identifier for the contact")
    private Long id;

    @NotBlank(message = "Name must not be empty")
    @Schema(example = "John Doe", description = "The full name of the contact")
    private String name;

    @Email(message = "Invalid email format")
    @Schema(example = "test@example.com", description = "The email address of the contact")
    private String email;

    @NotBlank(message = "Phone must not be empty")
    @Schema(example = "380-XXX-XXX-XXX", description = "The phone number of the contact")
    private String phone;
}
