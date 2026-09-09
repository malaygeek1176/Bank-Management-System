package com.bank.management.config;

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

import com.bank.management.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;


    /*
     * Account owned by securitytest customer.
     */
    private Account ownerAccount;


    /*
     * Account owned by another customer.
     */
    private Account otherCustomerAccount;


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

        User customerUser =
                userRepository.findByUsername(
                        "securitytest"
                ).orElseGet(() -> {

                    User user = new User();

                    user.setUsername("securitytest");

                    user.setPassword(
                            passwordEncoder.encode(
                                    "test-password"
                            )
                    );

                    user.setRole(customerRole);

                    user.setEnabled(true);

                    return userRepository.save(user);
                });


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


        // =========================
        // CUSTOMER PROFILE
        // =========================

        Customer customer =
                customerRepository
                        .findByUserUsername(
                                "securitytest"
                        )
                        .orElseGet(() -> {

                            Customer newCustomer =
                                    new Customer();

                            newCustomer.setUser(
                                    customerUser
                            );

                            newCustomer.setFirstName(
                                    "Security"
                            );

                            newCustomer.setLastName(
                                    "Test"
                            );

                            newCustomer.setEmail(
                                    "securitytest@test.com"
                            );

                            newCustomer.setPhone(
                                    "9000000001"
                            );

                            newCustomer.setAddress(
                                    "Delhi"
                            );

                            return customerRepository.save(
                                    newCustomer
                            );
                        });


        // =========================
        // SECOND CUSTOMER USER
        // =========================

        User secondUser =
                userRepository.findByUsername(
                        "securityother"
                ).orElseGet(() -> {

                    User user = new User();

                    user.setUsername(
                            "securityother"
                    );

                    user.setPassword(
                            passwordEncoder.encode(
                                    "other-password"
                            )
                    );

                    user.setRole(customerRole);

                    user.setEnabled(true);

                    return userRepository.save(user);
                });


        // =========================
        // SECOND CUSTOMER PROFILE
        // =========================

        Customer secondCustomer =
                customerRepository
                        .findByUserUsername(
                                "securityother"
                        )
                        .orElseGet(() -> {

                            Customer newCustomer =
                                    new Customer();

                            newCustomer.setUser(
                                    secondUser
                            );

                            newCustomer.setFirstName(
                                    "Other"
                            );

                            newCustomer.setLastName(
                                    "Customer"
                            );

                            newCustomer.setEmail(
                                    "securityother@test.com"
                            );

                            newCustomer.setPhone(
                                    "9000000002"
                            );

                            newCustomer.setAddress(
                                    "Mumbai"
                            );

                            return customerRepository.save(
                                    newCustomer
                            );
                        });


        // =========================
        // ACCOUNT TYPE
        // =========================

        AccountType accountType =
                accountTypeRepository
                        .findByName(
                                "SECURITY-TEST-SAVINGS"
                        )
                        .orElseGet(() -> {

                            AccountType type =
                                    new AccountType();

                            type.setName(
                                    "SECURITY-TEST-SAVINGS"
                            );

                            type.setDescription(
                                    "Security integration test account"
                            );

                            type.setMinimumBalance(
                                    new BigDecimal("0.00")
                            );

                            return accountTypeRepository.save(
                                    type
                            );
                        });


        // =========================
        // OWNER ACCOUNT
        // =========================

        ownerAccount =
                accountRepository
                        .findByAccountNumber(
                                "111111111111"
                        )
                        .orElseGet(() -> {

                            Account account =
                                    new Account();

                            account.setAccountNumber(
                                    "111111111111"
                            );

                            account.setCustomer(
                                    customer
                            );

                            account.setAccountType(
                                    accountType
                            );

                            account.setBalance(
                                    new BigDecimal("10000.00")
                            );

                            account.setStatus(
                                    AccountStatus.ACTIVE
                            );

                            return accountRepository.save(
                                    account
                            );
                        });


        // =========================
        // OTHER CUSTOMER ACCOUNT
        // =========================

        otherCustomerAccount =
                accountRepository
                        .findByAccountNumber(
                                "222222222222"
                        )
                        .orElseGet(() -> {

                            Account account =
                                    new Account();

                            account.setAccountNumber(
                                    "222222222222"
                            );

                            account.setCustomer(
                                    secondCustomer
                            );

                            account.setAccountType(
                                    accountType
                            );

                            account.setBalance(
                                    new BigDecimal("5000.00")
                            );

                            account.setStatus(
                                    AccountStatus.ACTIVE
                            );

                            return accountRepository.save(
                                    account
                            );
                        });
    }


    // =====================================================
    // CUSTOMER SECURITY TESTS
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


    @Test
    void customerGettingAllCustomers_shouldReturnForbidden()
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
                )
                .andExpect(
                        status().isForbidden()
                );
    }


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


    @Test
    void adminGettingAllCustomers_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securityadmin"
                );

        mockMvc.perform(
                        get("/api/customers")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


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


    // =====================================================
    // ACCOUNT OWNERSHIP TESTS
    // =====================================================


    @Test
    void customerGettingOwnAccount_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        get(
                                "/api/accounts/"
                                        + ownerAccount.getId()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    @Test
    void customerGettingOtherCustomerAccount_shouldReturnForbidden()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        get(
                                "/api/accounts/"
                                        + otherCustomerAccount.getId()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isForbidden()
                );
    }


    @Test
    void adminGettingOtherCustomerAccount_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securityadmin"
                );

        mockMvc.perform(
                        get(
                                "/api/accounts/"
                                        + otherCustomerAccount.getId()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    @Test
    void customerGettingOwnAccountByNumber_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        get(
                                "/api/accounts/number/"
                                        + ownerAccount.getAccountNumber()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    @Test
    void customerGettingOtherAccountByNumber_shouldReturnForbidden()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        get(
                                "/api/accounts/number/"
                                        + otherCustomerAccount
                                        .getAccountNumber()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isForbidden()
                );
    }


    @Test
    void customerGettingOwnBalance_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        get(
                                "/api/accounts/"
                                        + ownerAccount
                                        .getAccountNumber()
                                        + "/balance"
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    @Test
    void customerGettingOtherAccountBalance_shouldReturnForbidden()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        get(
                                "/api/accounts/"
                                        + otherCustomerAccount
                                        .getAccountNumber()
                                        + "/balance"
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isForbidden()
                );
    }


    @Test
    void gettingAccountWithoutToken_shouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        get(
                                "/api/accounts/"
                                        + ownerAccount.getId()
                        )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }


    // =====================================================
    // TRANSACTION OWNERSHIP TESTS
    // =====================================================


    /*
     * CUSTOMER should be able to deposit
     * into their own account.
     */
    @Test
    void customerDepositingIntoOwnAccount_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        post("/api/transactions/deposit")
                                .param(
                                        "accountNumber",
                                        ownerAccount
                                                .getAccountNumber()
                                )
                                .param(
                                        "amount",
                                        "1.00"
                                )
                                .param(
                                        "description",
                                        "Security test deposit"
                                )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    /*
     * CUSTOMER must not deposit
     * into another customer's account.
     */
    @Test
    void customerDepositingIntoOtherAccount_shouldReturnForbidden()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        post("/api/transactions/deposit")
                                .param(
                                        "accountNumber",
                                        otherCustomerAccount
                                                .getAccountNumber()
                                )
                                .param(
                                        "amount",
                                        "1.00"
                                )
                                .param(
                                        "description",
                                        "Unauthorized deposit"
                                )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isForbidden()
                );
    }


    /*
     * ADMIN should be able to deposit
     * into another customer's account.
     */
    @Test
    void adminDepositingIntoOtherAccount_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securityadmin"
                );

        mockMvc.perform(
                        post("/api/transactions/deposit")
                                .param(
                                        "accountNumber",
                                        otherCustomerAccount
                                                .getAccountNumber()
                                )
                                .param(
                                        "amount",
                                        "1.00"
                                )
                                .param(
                                        "description",
                                        "Admin security test deposit"
                                )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    @Test
    void depositingWithoutToken_shouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        post("/api/transactions/deposit")
                                .param(
                                        "accountNumber",
                                        ownerAccount
                                                .getAccountNumber()
                                )
                                .param(
                                        "amount",
                                        "1.00"
                                )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }


    /*
     * CUSTOMER should be able to withdraw
     * from their own account.
     */
    @Test
    void customerWithdrawingFromOwnAccount_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        post("/api/transactions/withdraw")
                                .param(
                                        "accountNumber",
                                        ownerAccount
                                                .getAccountNumber()
                                )
                                .param(
                                        "amount",
                                        "1.00"
                                )
                                .param(
                                        "description",
                                        "Security test withdrawal"
                                )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    /*
     * CUSTOMER must not withdraw
     * from another customer's account.
     */
    @Test
    void customerWithdrawingFromOtherAccount_shouldReturnForbidden()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        post("/api/transactions/withdraw")
                                .param(
                                        "accountNumber",
                                        otherCustomerAccount
                                                .getAccountNumber()
                                )
                                .param(
                                        "amount",
                                        "1.00"
                                )
                                .param(
                                        "description",
                                        "Unauthorized withdrawal"
                                )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isForbidden()
                );
    }


    /*
     * ADMIN should be able to withdraw
     * from another customer's account.
     */
    @Test
    void adminWithdrawingFromOtherAccount_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securityadmin"
                );

        mockMvc.perform(
                        post("/api/transactions/withdraw")
                                .param(
                                        "accountNumber",
                                        otherCustomerAccount
                                                .getAccountNumber()
                                )
                                .param(
                                        "amount",
                                        "1.00"
                                )
                                .param(
                                        "description",
                                        "Admin security test withdrawal"
                                )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    @Test
    void withdrawingWithoutToken_shouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        post("/api/transactions/withdraw")
                                .param(
                                        "accountNumber",
                                        ownerAccount
                                                .getAccountNumber()
                                )
                                .param(
                                        "amount",
                                        "1.00"
                                )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }


    /*
     * CUSTOMER should be able to transfer
     * from their own account.
     *
     * Receiver can belong to another customer.
     */
    @Test
    void customerTransferringFromOwnAccount_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        String requestBody = """
                {
                    "senderAccountNumber": "%s",
                    "receiverAccountNumber": "%s",
                    "amount": 1.00,
                    "description": "Security test transfer"
                }
                """.formatted(
                ownerAccount.getAccountNumber(),
                otherCustomerAccount.getAccountNumber()
        );

        mockMvc.perform(
                        post("/api/transactions/transfer")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    /*
     * CUSTOMER must not transfer money
     * from another customer's account.
     */
    @Test
    void customerTransferringFromOtherAccount_shouldReturnForbidden()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        String requestBody = """
                {
                    "senderAccountNumber": "%s",
                    "receiverAccountNumber": "%s",
                    "amount": 1.00,
                    "description": "Unauthorized transfer"
                }
                """.formatted(
                otherCustomerAccount.getAccountNumber(),
                ownerAccount.getAccountNumber()
        );

        mockMvc.perform(
                        post("/api/transactions/transfer")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isForbidden()
                );
    }


    /*
     * ADMIN should be able to transfer
     * from another customer's account.
     */
    @Test
    void adminTransferringFromOtherAccount_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securityadmin"
                );

        String requestBody = """
                {
                    "senderAccountNumber": "%s",
                    "receiverAccountNumber": "%s",
                    "amount": 1.00,
                    "description": "Admin security test transfer"
                }
                """.formatted(
                otherCustomerAccount.getAccountNumber(),
                ownerAccount.getAccountNumber()
        );

        mockMvc.perform(
                        post("/api/transactions/transfer")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    @Test
    void transferringWithoutToken_shouldReturnUnauthorized()
            throws Exception {

        String requestBody = """
                {
                    "senderAccountNumber": "%s",
                    "receiverAccountNumber": "%s",
                    "amount": 1.00,
                    "description": "Unauthorized transfer"
                }
                """.formatted(
                ownerAccount.getAccountNumber(),
                otherCustomerAccount.getAccountNumber()
        );

        mockMvc.perform(
                        post("/api/transactions/transfer")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }


    /*
     * CUSTOMER should be able to see
     * transactions of their own account.
     */
    @Test
    void customerGettingOwnTransactions_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        get(
                                "/api/transactions/account/"
                                        + ownerAccount
                                        .getAccountNumber()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    /*
     * CUSTOMER must not see transactions
     * of another customer's account.
     */
    @Test
    void customerGettingOtherAccountTransactions_shouldReturnForbidden()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securitytest"
                );

        mockMvc.perform(
                        get(
                                "/api/transactions/account/"
                                        + otherCustomerAccount
                                        .getAccountNumber()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isForbidden()
                );
    }


    /*
     * ADMIN should be able to see transactions
     * of another customer's account.
     */
    @Test
    void adminGettingOtherAccountTransactions_shouldAllowAccess()
            throws Exception {

        String token =
                jwtService.generateToken(
                        "securityadmin"
                );

        mockMvc.perform(
                        get(
                                "/api/transactions/account/"
                                        + otherCustomerAccount
                                        .getAccountNumber()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                );
    }


    @Test
    void gettingTransactionsWithoutToken_shouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        get(
                                "/api/transactions/account/"
                                        + ownerAccount
                                        .getAccountNumber()
                        )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }
}