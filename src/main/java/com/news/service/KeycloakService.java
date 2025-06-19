package com.news.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.news.dto.TokenResponseDTO;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class KeycloakService {

    @Value("${keycloak.server-url}")
    private String keycloakUrl;
    @Value("${keycloak.realm}")
    private String keycloakRealm;
    @Value("${keycloak-admin.username}")
    private String keycloakAdminUser;
    @Value("${keycloak-admin.password}")
    private String keycloakAdminPassword;
    @Value("${keycloak.resource}")
    private String keycloakClientId;
    @Value("${keycloak.credentials.secret}")
    private String keycloakClientSecret;

    private Keycloak keycloak;
    private final RestTemplate restTemplate;

    public KeycloakService() {
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    private void initKeycloak() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(this.keycloakUrl)
                .realm(keycloakRealm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .username(this.keycloakAdminUser)
                .password(this.keycloakAdminPassword)
                .build();
    }

    @Transactional
    public void registerUser(String username, String email, String password, String firstName, String lastName) {
        try {
            List<UserRepresentation> existingUsers = keycloak.realm(keycloakRealm).users()
                    .searchByUsername(username, true);
            if (!existingUsers.isEmpty()) {
                throw new IllegalArgumentException("User with username " + username + " already exists");
            }

            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmailVerified(false);

            // Create user in Keycloak
            Response response = keycloak.realm(keycloakRealm).users().create(user);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create user in Keycloak");
            }

            String userId = CreatedResponseUtil.getCreatedId(response);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);

            keycloak.realm(keycloakRealm).users().get(userId).resetPassword(credential);

            RoleRepresentation userRole = keycloak.realm(keycloakRealm).roles().get("ROLE_READER").toRepresentation();
            keycloak.realm(keycloakRealm).users().get(userId).roles().realmLevel().add(Collections.singletonList(userRole));

            log.info("User {} successfully registered", username);
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            throw new RuntimeException("Failed to register user: " + e.getMessage());
        }
    }

    @Transactional
    public TokenResponseDTO login(String username, String password) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "password");
            map.add("client_id", keycloakClientId);
            map.add("client_secret", keycloakClientSecret);
            map.add("username", username);
            map.add("password", password);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            String tokenUrl = keycloakUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";
            ResponseEntity<TokenResponseDTO> response = restTemplate.postForEntity(
                    tokenUrl,
                    request,
                    TokenResponseDTO.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("User {} successfully logged in", username);
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to login user");
            }
        } catch (Exception e) {
            log.error("Error logging in user: {}", e.getMessage());
            throw new RuntimeException("Failed to login: " + e.getMessage());
        }
    }
}
