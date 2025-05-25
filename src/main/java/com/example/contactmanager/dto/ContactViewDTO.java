package com.example.contactmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ContactViewDTO {

    @Schema(example = "1", description = "Unique identifier for the contact")
    private Long id;

    @Schema(example = "John Doe", description = "The full name of the contact")
    private String name;

    @Schema(example = "test@example.com", description = "The email address of the contact")
    private String email;

    @Schema(example = "380-XXX-XXX-XXX", description = "The phone number of the contact")
    private String phone;
}
