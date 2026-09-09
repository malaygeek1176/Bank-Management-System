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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
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

        // =========================
        // CUSTOMER ROLE
        // =========================

        Role customerRole =
                roleRepository.findByName("CUSTOMER")
                        .orElseGet(() ->
                                roleRepository.save(
                                        new Role("CUSTOMER")
                                )
                        );


        // =========================
        // ADMIN ROLE
        // =========================

        Role adminRole =
                roleRepository.findByName("ADMIN")
                        .orElseGet(() ->
                                roleRepository.save(
                                        new Role("ADMIN")
                                )
                        );


        // =========================
        // CUSTOMER TEST USER
        // =========================

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


        // =========================
        // ADMIN TEST USER
        // =========================

        if (userRepository.findByUsername(
                "securityadmin"
        ).isEmpty()) {

            User admin = new User();

            admin.setUsername("securityadmin");

            admin.setPassword(
                    passwordEncoder.encode(
                            "admin-password"
                    )
            );

            admin.setRole(adminRole);

            admin.setEnabled(true);

            userRepository.save(admin);
        }
    }


    // =====================================================
    // 1. NO TOKEN
    // =====================================================

    @Test
    void getCustomersWithoutToken_shouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        get("/api/customers")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }


    // =====================================================
    // 2. VALID CUSTOMER TOKEN
    // =====================================================

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
                .andExpect(
                        status().isOk()
                );
    }


    // =====================================================
    // 3. CUSTOMER CANNOT DELETE CUSTOMER
    // =====================================================

    @Test
    void customerDeletingCustomer_shouldReturnForbidden()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        delete("/api/customers/999999")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                )
                .andExpect(
                        status().isForbidden()
                );
    }


    // =====================================================
    // 4. ADMIN CAN ACCESS DELETE ENDPOINT
    // =====================================================

    @Test
    void adminDeletingNonExistingCustomer_shouldReturnNotFound()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securityadmin"
                );

        mockMvc.perform(
                        delete("/api/customers/999999")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                )
                .andExpect(
                        status().isNotFound()
                );
    }


    // =====================================================
    // 5. NO TOKEN CANNOT DELETE CUSTOMER
    // =====================================================

    @Test
    void deletingCustomerWithoutToken_shouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        delete("/api/customers/999999")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }
}