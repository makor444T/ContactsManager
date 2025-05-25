package com.example.contactmanager.service.impl;

import com.example.contactmanager.exception.ExceptionUtils;
import com.example.contactmanager.exception.ResourceNotFoundException;
import com.example.contactmanager.model.Contact;
import com.example.contactmanager.repository.ContactRepository;
import com.example.contactmanager.service.ContactService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact createContact(Contact contact) {
        contact.setId(null);
        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Long id, Contact contact) {
        Contact existing = contactRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.createContactNotFoundException(id));
        existing.setName(contact.getName());
        existing.setEmail(contact.getEmail());
        existing.setPhone(contact.getPhone());
        return contactRepository.save(existing);
    }

    @Override
    public void deleteContact(Long id) {
        Contact existing = contactRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.createContactNotFoundException(id));
        contactRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Contact getContact(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.createContactNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contact> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        if (contacts.isEmpty()) {
            throw new ResourceNotFoundException("Contacts not found");
        }
        return contacts;
    }
}
