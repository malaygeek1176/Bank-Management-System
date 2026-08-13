package com.bank.management.repository;

import com.bank.management.entity.Role;
import com.bank.management.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldSaveAndFindUserByUsername() {

        // Create Role
        Role role = new Role("CUSTOMER");
        role = roleRepository.save(role);

        // Create User
        User user = new User();

        user.setUsername("rahul123");
        user.setPassword("test-password");
        user.setRole(role);

        // Save User
        User savedUser = userRepository.save(user);

        // Verify ID generated
        assertNotNull(savedUser.getId());

        // Find by username
        assertTrue(
                userRepository
                        .findByUsername("rahul123")
                        .isPresent()
        );

        // Check username exists
        assertTrue(
                userRepository
                        .existsByUsername("rahul123")
        );
    }
}