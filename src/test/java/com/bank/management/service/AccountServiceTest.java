package com.bank.management.service;

import com.bank.management.dto.response.AccountResponse;
import com.bank.management.entity.Account;
import com.bank.management.entity.AccountStatus;
import com.bank.management.entity.AccountType;
import com.bank.management.entity.Customer;
import com.bank.management.entity.Role;
import com.bank.management.entity.User;
import com.bank.management.repository.AccountRepository;
import com.bank.management.repository.AccountTypeRepository;
import com.bank.management.repository.CustomerRepository;
import com.bank.management.repository.RoleRepository;
import com.bank.management.repository.UserRepository;
import com.bank.management.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Import(AccountServiceTest.TestConfig.class)
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;


    @Test
    void shouldCreateAccountSuccessfully() {

        // 1. Create Role
        Role role = roleRepository
                .findByName("CUSTOMER")
                .orElseGet(() -> {

                    Role newRole = new Role();
                    newRole.setName("CUSTOMER");

                    return roleRepository.save(newRole);
                });


        // 2. Create User
        User user = new User();
        user.setUsername("testuser123");
        user.setPassword("test-password");
        user.setRole(role);

        user = userRepository.save(user);


        // 3. Create Customer
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setFirstName("Rahul");
        customer.setLastName("Sharma");
        customer.setEmail("rahul@test.com");
        customer.setPhone("9876543210");
        customer.setAddress("Delhi");

        customer = customerRepository.save(customer);


        // 4. Create Account Type
        AccountType accountType = new AccountType();
        accountType.setName("TEST-SAVINGS");
        accountType.setDescription("Savings Account");
        accountType.setMinimumBalance(
                new BigDecimal("1000.00")
        );

        accountType = accountTypeRepository.save(accountType);


        // 5. Create Account through Service
        AccountResponse account =
                accountService.createAccount(
                        customer.getId(),
                        accountType.getId()
                );


        // 6. Verify Account
        assertNotNull(account.getId());

        assertNotNull(account.getAccountNumber());

        assertEquals(
                12,
                account.getAccountNumber().length()
        );

        assertEquals(
                BigDecimal.ZERO,
                account.getBalance()
        );

        assertEquals(
                AccountStatus.ACTIVE.name(),
                account.getStatus()
        );

        assertEquals(
                customer.getId(),
                account.getCustomerId()
        );

        assertEquals(
                accountType.getId(),
                account.getAccountTypeId()
        );
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        AccountService accountService(
                AccountRepository accountRepository,
                CustomerRepository customerRepository,
                AccountTypeRepository accountTypeRepository) {

            return new AccountServiceImpl(
                    accountRepository,
                    customerRepository,
                    accountTypeRepository
            );
        }
    }
}