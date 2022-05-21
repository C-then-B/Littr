package com.littr.littr;

import com.littr.littr.shared.GenericResponse;
import com.littr.littr.user.User;
import com.littr.littr.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {
    private static final String API_1_0_USERS = "/api/1.0/users";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void valid_user_can_post() {
        User user = createValidUser();

        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class, "");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void valid_user_receives_success_message_on_post() {
        User user = createValidUser();

        ResponseEntity<GenericResponse> response = testRestTemplate.postForEntity(API_1_0_USERS, user,
                GenericResponse.class, "");

        assertThat(response.getBody().getMessage()).isNotNull();
    }

    @Test
    public void valid_user_can_be_saved_to_database() {
        User user = createValidUser();

        testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class, "");

        assertThat(userRepository.count()).isEqualTo(1);
    }

    private User createValidUser() {
        User user = new User();

        // >=4 chars
        user.setUsername("test-username");
        user.setDisplayName("test-displayName");

        // >=8 chars with >=1 digit, >=1 special, >=1 lowercase, >=1 uppercase
        user.setPassword("Abcdef1%");
        return user;
    }
}