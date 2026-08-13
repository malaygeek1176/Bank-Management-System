package com.bank.management.repository;

import com.bank.management.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
        //Ye bolta hai ki Mere configured PostgreSQL DataSource ko replace mat karo
)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldSaveAndFindRole() {

        Role role = new Role("CUSTOMER");

        Role savedRole = roleRepository.save(role);

        assertNotNull(savedRole.getId());

        Optional<Role> foundRole =
                roleRepository.findByName("CUSTOMER");

        assertTrue(foundRole.isPresent());

        assertEquals("CUSTOMER", foundRole.get().getName());
    }
}