package com.bank.management.config;

import com.bank.management.entity.Role;
import com.bank.management.entity.User;
import com.bank.management.repository.RoleRepository;
import com.bank.management.repository.UserRepository;
import com.bank.management.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "logging.level.org.springframework.security=DEBUG"
        }
)
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {

        Role customerRole =
                roleRepository.findByName("CUSTOMER")
                        .orElseGet(() ->
                                roleRepository.save(
                                        new Role("CUSTOMER")
                                )
                        );

        if (userRepository.findByUsername(
                "securitytest"
        ).isEmpty()) {

            User user = new User();

            user.setUsername("securitytest");

            user.setPassword(
                    passwordEncoder.encode(
                            "test-password"
                    )
            );

            user.setRole(customerRole);

            user.setEnabled(true);

            userRepository.save(user);
        }
    }


    @Test
    void getCustomersWithoutToken_shouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        get("/api/customers")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                )
                .andExpect(status().isUnauthorized());
    }


    @Test
    void getCustomersWithValidToken_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        get("/api/customers")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                )
                .andExpect(status().isOk());
    }
}