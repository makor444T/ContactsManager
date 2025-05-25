package com.example.contactmanager;

import com.example.contactmanager.dto.ContactCreateDTO;
import com.example.contactmanager.dto.ContactViewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactServiceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void testCreateAndGetContact_Positive() {
        ContactCreateDTO dto = new ContactCreateDTO();
        dto.setName("Alice");
        dto.setEmail("alice@example.com");
        dto.setPhone("+11111111");

        HttpEntity<ContactCreateDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<ContactViewDTO> createResponse = restTemplate.postForEntity("/api/contacts", request, ContactViewDTO.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        Long id = createResponse.getBody().getId();

        ResponseEntity<ContactViewDTO> getResponse = restTemplate.exchange(
                "/api/contacts/" + id,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ContactViewDTO.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getName()).isEqualTo("Alice");
    }

    @Test
    void testGetContact_Negative_NotFound() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/contacts/99999",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("not found");
    }

    @Test
    void testGetAllContacts_Positive() {
        ContactCreateDTO dto = new ContactCreateDTO();
        dto.setName("Bob");
        dto.setEmail("bob@example.com");
        dto.setPhone("+22222222");
        restTemplate.postForEntity("/api/contacts", new HttpEntity<>(dto, headers), ContactViewDTO.class);

        ResponseEntity<ContactViewDTO[]> response = restTemplate.exchange(
                "/api/contacts",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ContactViewDTO[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testUpdateContact_Positive() {
        ContactCreateDTO dto = new ContactCreateDTO();
        dto.setName("Charlie");
        dto.setEmail("charlie@example.com");
        dto.setPhone("+33333333");
        ContactViewDTO created = restTemplate.postForEntity("/api/contacts", new HttpEntity<>(dto, headers), ContactViewDTO.class).getBody();

        dto.setName("Charlie Updated");
        HttpEntity<ContactCreateDTO> updateRequest = new HttpEntity<>(dto, headers);
        assert created != null;
        ResponseEntity<ContactViewDTO> updateResponse = restTemplate.exchange(
                "/api/contacts/" + created.getId(),
                HttpMethod.PUT,
                updateRequest,
                ContactViewDTO.class
        );
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(updateResponse.getBody()).getName()).isEqualTo("Charlie Updated");
    }

    @Test
    void testUpdateContact_Negative_NotFound() {
        ContactCreateDTO dto = new ContactCreateDTO();
        dto.setName("NonExistent");
        dto.setEmail("no@example.com");
        dto.setPhone("000");

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/contacts/99999",
                HttpMethod.PUT,
                new HttpEntity<>(dto, headers),
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("not found");
    }

    @Test
    void testDeleteContact_Positive() {
        ContactCreateDTO dto = new ContactCreateDTO();
        dto.setName("ToDelete");
        dto.setEmail("delete@example.com");
        dto.setPhone("+44444444");
        ContactViewDTO created = restTemplate.postForEntity("/api/contacts", new HttpEntity<>(dto, headers), ContactViewDTO.class).getBody();

        assert created != null;
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/contacts/" + created.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getAfterDelete = restTemplate.exchange(
                "/api/contacts/" + created.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteContact_Negative_NotFound() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/contacts/99999",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("not found");
    }
}
