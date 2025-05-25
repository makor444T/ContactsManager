package com.example.contactmanager.controller;

import com.example.contactmanager.dto.ContactCreateDTO;
import com.example.contactmanager.dto.ContactViewDTO;
import com.example.contactmanager.mapper.ContactMapper;
import com.example.contactmanager.model.Contact;
import com.example.contactmanager.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Contacts", description = "CRUD operations for managing contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @Operation(summary = "Create a Contact", description = "Creates a new contact and saves it to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created - Contact successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactViewDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have enough rights", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found - Contact could not be created or already exists",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<ContactViewDTO> createContact(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The contact object to be created")
            @Valid @RequestBody ContactCreateDTO contactCreateDTO) {
        Contact contact = ContactMapper.fromDTO(contactCreateDTO);
        Contact created = contactService.createContact(contact);
        return new ResponseEntity<>(ContactMapper.toDTO(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve a Contact", description = "Retrieves a contact by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Contact retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ContactViewDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have enough rights", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found - No contact found with the given ID",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContactViewDTO> getContact(@PathVariable Long id) {
        Contact contact = contactService.getContact(id);
        if (contact == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ContactMapper.toDTO(contact));
    }

    @Operation(summary = "Retrieve All Contacts", description = "Retrieves all contacts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Contacts retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ContactViewDTO[].class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have enough rights", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found - No contacts found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ContactViewDTO>> getAllContacts() {
        List<Contact> contacts = Optional.ofNullable(contactService.getAllContacts()).orElse(List.of());
        List<ContactViewDTO> dtos = contacts.stream()
                .map(ContactMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Update a Contact", description = "Updates the information of a contact specified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Contact updated successfully",
                    content = @Content(schema = @Schema(implementation = ContactViewDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have enough rights", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found - Contact with the given ID does not exist",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContactViewDTO> updateContact(
            @Parameter(description = "The ID of the contact to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The contact object with updated information")
            @Valid @RequestBody ContactCreateDTO contactCreateDTO) {
        Contact contact = ContactMapper.fromDTO(contactCreateDTO);
        Contact updated = contactService.updateContact(id, contact);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ContactMapper.toDTO(updated));
    }

    @Operation(summary = "Delete a Contact", description = "Deletes a contact identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content - Contact deleted successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have enough rights", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found - Contact with the specified ID does not exist", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(
            @Parameter(description = "The ID of the contact to delete", required = true)
            @PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
