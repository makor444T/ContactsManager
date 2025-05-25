package com.example.contactmanager.mapper;

import com.example.contactmanager.dto.ContactCreateDTO;
import com.example.contactmanager.dto.ContactViewDTO;
import com.example.contactmanager.model.Contact;

public class ContactMapper {

    private ContactMapper() {
    }

    public static ContactViewDTO toDTO(Contact contact) {
        ContactViewDTO dto = new ContactViewDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setEmail(contact.getEmail());
        dto.setPhone(contact.getPhone());
        return dto;
    }

    public static Contact fromDTO(ContactCreateDTO dto) {
        return Contact.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }
}

