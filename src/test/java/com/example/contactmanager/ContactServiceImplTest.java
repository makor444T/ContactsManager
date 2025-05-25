package com.example.contactmanager;

import com.example.contactmanager.exception.ResourceNotFoundException;
import com.example.contactmanager.model.Contact;
import com.example.contactmanager.repository.ContactRepository;
import com.example.contactmanager.service.impl.ContactServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ContactServiceImplTest {

    private ContactRepository contactRepository;
    private ContactServiceImpl contactService;

    @BeforeEach
    void setUp() {
        contactRepository = mock(ContactRepository.class);
        contactService = new ContactServiceImpl(contactRepository);
    }

    @Test
    void createContact_ShouldSaveContact() {
        Contact contact = new Contact();
        contact.setName("Test");
        contact.setEmail("test@example.com");
        contact.setPhone("123");

        Contact savedContact = new Contact();
        savedContact.setId(1L);
        savedContact.setName("Test");
        savedContact.setEmail("test@example.com");
        savedContact.setPhone("123");

        when(contactRepository.save(any(Contact.class))).thenReturn(savedContact);

        Contact result = contactService.createContact(contact);

        assertThat(result.getId()).isEqualTo(1L);
        verify(contactRepository).save(contact);
        assertThat(contact.getId()).isNull();
    }

    @Test
    void updateContact_WhenContactExists_ShouldUpdateAndReturn() {
        Contact existing = new Contact();
        existing.setId(1L);
        existing.setName("Old");
        existing.setEmail("old@example.com");
        existing.setPhone("000");

        Contact updated = new Contact();
        updated.setName("New");
        updated.setEmail("new@example.com");
        updated.setPhone("111");

        when(contactRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(contactRepository.save(any(Contact.class))).thenAnswer(i -> i.getArgument(0));

        Contact result = contactService.updateContact(1L, updated);

        assertThat(result.getName()).isEqualTo("New");
        assertThat(result.getEmail()).isEqualTo("new@example.com");
        assertThat(result.getPhone()).isEqualTo("111");
        verify(contactRepository).findById(1L);
        verify(contactRepository).save(existing);
    }

    @Test
    void updateContact_WhenContactNotFound_ShouldThrow() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        Contact updated = new Contact();

        assertThatThrownBy(() -> contactService.updateContact(1L, updated))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(contactRepository).findById(1L);
        verify(contactRepository, never()).save(any());
    }

    @Test
    void deleteContact_WhenContactExists_ShouldDelete() {
        Contact existing = new Contact();
        existing.setId(1L);

        when(contactRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(contactRepository).delete(existing);

        contactService.deleteContact(1L);

        verify(contactRepository).findById(1L);
        verify(contactRepository).delete(existing);
    }

    @Test
    void deleteContact_WhenContactNotFound_ShouldThrow() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.deleteContact(1L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(contactRepository).findById(1L);
        verify(contactRepository, never()).delete(any());
    }

    @Test
    void getContact_WhenContactExists_ShouldReturn() {
        Contact existing = new Contact();
        existing.setId(1L);

        when(contactRepository.findById(1L)).thenReturn(Optional.of(existing));

        Contact result = contactService.getContact(1L);

        assertThat(result).isEqualTo(existing);
        verify(contactRepository).findById(1L);
    }

    @Test
    void getContact_WhenContactNotFound_ShouldThrow() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.getContact(1L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(contactRepository).findById(1L);
    }

    @Test
    void getAllContacts_WhenNotEmpty_ShouldReturnList() {
        Contact contact = new Contact();
        contact.setId(1L);
        when(contactRepository.findAll()).thenReturn(List.of(contact));

        List<Contact> result = contactService.getAllContacts();

        assertThat(result).hasSize(1);
        verify(contactRepository).findAll();
    }

    @Test
    void getAllContacts_WhenEmpty_ShouldThrow() {
        when(contactRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> contactService.getAllContacts())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Contacts not found");
        verify(contactRepository).findAll();
    }
}
