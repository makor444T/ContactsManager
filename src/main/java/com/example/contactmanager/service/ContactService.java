package com.example.contactmanager.service;

import com.example.contactmanager.model.Contact;

import java.util.List;

public interface ContactService {
    Contact createContact(Contact contact);

    Contact updateContact(Long id, Contact contact);

    void deleteContact(Long id);

    Contact getContact(Long id);

    List<Contact> getAllContacts();
}
