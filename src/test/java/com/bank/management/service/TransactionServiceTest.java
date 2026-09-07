package com.bank.management.service;

import com.bank.management.entity.Account;
import com.bank.management.entity.AccountStatus;
import com.bank.management.entity.AccountType;
import com.bank.management.entity.Customer;
import com.bank.management.entity.Role;
import com.bank.management.entity.Transaction;
import com.bank.management.entity.TransactionStatus;
import com.bank.management.entity.User;
import com.bank.management.repository.AccountRepository;
import com.bank.management.repository.AccountTypeRepository;
import com.bank.management.repository.CustomerRepository;
import com.bank.management.repository.RoleRepository;
import com.bank.management.repository.TransactionRepository;
import com.bank.management.repository.TransactionTypeRepository;
import com.bank.management.repository.UserRepository;
import com.bank.management.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Import(TransactionServiceTest.TestConfig.class)
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    private Account account;


    @BeforeEach
    void setUp() {

        // Create Role
        Role role = this.roleRepository
                .findByName("CUSTOMER")
                .orElseGet(() -> {

                    Role newRole = new Role();
                    newRole.setName("CUSTOMER");

                    return this.roleRepository.save(newRole);
                });


        // Create User
        User user = new User();

        user.setUsername("transactionuser");
        user.setPassword("test-password");
        user.setRole(role);

        user = userRepository.save(user);


        // Create Customer
        Customer customer = new Customer();

        customer.setUser(user);
        customer.setFirstName("Rahul");
        customer.setLastName("Sharma");
        customer.setEmail("transaction@test.com");
        customer.setPhone("9876543210");
        customer.setAddress("Delhi");

        customer = customerRepository.save(customer);


        // Create Account Type
        AccountType accountType = new AccountType();

        accountType.setName("TEST-SAVINGS");
        accountType.setDescription("Test Savings Account");
        accountType.setMinimumBalance(
                new BigDecimal("0.00")
        );

        accountType = accountTypeRepository.save(accountType);


        // Create Account
        account = new Account();

        account.setAccountNumber("123456789012");
        account.setCustomer(customer);
        account.setAccountType(accountType);
        account.setBalance(new BigDecimal("10000.00"));
        account.setStatus(AccountStatus.ACTIVE);

        account = accountRepository.save(account);


        // Create Transaction Types
        createTransactionType("DEPOSIT");
        createTransactionType("WITHDRAWAL");
        createTransactionType("TRANSFER");
    }


    private void createTransactionType(String name) {

        if (transactionTypeRepository.findByName(name).isEmpty()) {

            var transactionType = new com.bank.management.entity.TransactionType();

            transactionType.setName(name);
            transactionType.setDescription(
                    name + " transaction"
            );

            transactionTypeRepository.save(transactionType);
        }
    }


    @Test
    void shouldDepositMoneySuccessfully() {

        Transaction transaction =
                transactionService.deposit(
                        account.getAccountNumber(),
                        new BigDecimal("5000.00"),
                        "Test deposit"
                );


        assertNotNull(transaction);

        assertNotNull(
                transaction.getTransactionReference()
        );

        assertEquals(
                new BigDecimal("5000.00"),
                transaction.getAmount()
        );

        assertEquals(
                new BigDecimal("15000.00"),
                transaction.getBalanceAfter()
        );

        assertEquals(
                TransactionStatus.SUCCESS,
                transaction.getStatus()
        );


        Account updatedAccount =
                accountRepository
                        .findByAccountNumber(
                                account.getAccountNumber()
                        )
                        .orElseThrow();

        assertEquals(
                new BigDecimal("15000.00"),
                updatedAccount.getBalance()
        );
    }


    @Test
    void shouldWithdrawMoneySuccessfully() {

        Transaction transaction =
                transactionService.withdraw(
                        account.getAccountNumber(),
                        new BigDecimal("2000.00"),
                        "Test withdrawal"
                );


        assertNotNull(transaction);

        assertEquals(
                new BigDecimal("2000.00"),
                transaction.getAmount()
        );

        assertEquals(
                new BigDecimal("8000.00"),
                transaction.getBalanceAfter()
        );

        assertEquals(
                TransactionStatus.SUCCESS,
                transaction.getStatus()
        );


        Account updatedAccount =
                accountRepository
                        .findByAccountNumber(
                                account.getAccountNumber()
                        )
                        .orElseThrow();

        assertEquals(
                new BigDecimal("8000.00"),
                updatedAccount.getBalance()
        );
    }


    @Test
    void shouldRejectInsufficientBalance() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        transactionService.withdraw(
                                account.getAccountNumber(),
                                new BigDecimal("20000.00"),
                                "Insufficient balance test"
                        )
        );


        Account updatedAccount =
                accountRepository
                        .findByAccountNumber(
                                account.getAccountNumber()
                        )
                        .orElseThrow();

        assertEquals(
                new BigDecimal("10000.00"),
                updatedAccount.getBalance()
        );
    }


    @Test
    void shouldGetAccountTransactionsSuccessfully() {

        // Create a transaction
        transactionService.deposit(
                account.getAccountNumber(),
                new BigDecimal("2000"),
                "Test deposit"
        );

        // Fetch transaction history
        List<Transaction> transactions =
                transactionService.getAccountTransactions(
                        account.getAccountNumber()
                );

        // Verify history
        assertNotNull(transactions);
        assertEquals(1, transactions.size());

        Transaction transaction = transactions.get(0);

        assertEquals(
                new BigDecimal("2000"),
                transaction.getAmount()
        );

        assertEquals(
                0,
                new BigDecimal("12000").compareTo(
                        transaction.getBalanceAfter()
                )
        );

        assertEquals(
                TransactionStatus.SUCCESS,
                transaction.getStatus()
        );
    }


    @Test
    void shouldRejectInvalidAmount() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        transactionService.deposit(
                                account.getAccountNumber(),
                                new BigDecimal("-500.00"),
                                "Invalid amount test"
                        )
        );


        assertThrows(
                IllegalArgumentException.class,
                () ->
                        transactionService.withdraw(
                                account.getAccountNumber(),
                                BigDecimal.ZERO,
                                "Zero amount test"
                        )
        );
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        TransactionService transactionService(
                AccountRepository accountRepository,
                TransactionRepository transactionRepository,
                TransactionTypeRepository transactionTypeRepository) {

            return new TransactionServiceImpl(
                    accountRepository,
                    transactionRepository,
                    transactionTypeRepository
            );
        }
    }





    @Test
    void shouldTransferMoneySuccessfully() {

        // Create second customer
        Role role = roleRepository.findByName("CUSTOMER")
                .orElseThrow();

        User secondUser = new User();
        secondUser.setUsername("receiveruser");
        secondUser.setPassword("test-password");
        secondUser.setRole(role);

        secondUser = userRepository.save(secondUser);


        Customer secondCustomer = new Customer();

        secondCustomer.setUser(secondUser);
        secondCustomer.setFirstName("Priya");
        secondCustomer.setLastName("Sharma");
        secondCustomer.setEmail("receiver@test.com");
        secondCustomer.setPhone("9876543211");
        secondCustomer.setAddress("Mumbai");

        secondCustomer = customerRepository.save(secondCustomer);


        // Create second account
        AccountType accountType =
                accountTypeRepository
                        .findByName("TEST-SAVINGS")
                        .orElseThrow();

        final Account receiverAccount = new Account();

        receiverAccount.setAccountNumber("987654321098");
        receiverAccount.setCustomer(secondCustomer);
        receiverAccount.setAccountType(accountType);
        receiverAccount.setBalance(
                new BigDecimal("5000.00")
        );
        receiverAccount.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(receiverAccount);


        long initialTransactionCount = transactionRepository.count();
        // Transfer ₹3,000
        Transaction transaction =
                transactionService.transfer(
                        account.getAccountNumber(),
                        receiverAccount.getAccountNumber(),
                        new BigDecimal("3000.00"),
                        "Test transfer"
                );


        // Verify sender
        Account updatedSender =
                accountRepository
                        .findByAccountNumber(
                                account.getAccountNumber()
                        )
                        .orElseThrow();

        assertEquals(
                new BigDecimal("7000.00"),
                updatedSender.getBalance()
        );


        // Verify receiver
        Account updatedReceiver =
                accountRepository
                        .findByAccountNumber(
                                receiverAccount.getAccountNumber()
                        )
                        .orElseThrow();

        assertEquals(
                new BigDecimal("8000.00"),
                updatedReceiver.getBalance()
        );


        // Verify transaction
        assertNotNull(transaction);

        assertEquals(
                TransactionStatus.SUCCESS,
                transaction.getStatus()
        );


        // Two transactions should exist
        long transactionCount = transactionRepository.count();

        assertEquals(2, transactionCount - initialTransactionCount);
    }



    @Test
    void shouldRejectTransferToSameAccount() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        transactionService.transfer(
                                account.getAccountNumber(),
                                account.getAccountNumber(),
                                new BigDecimal("1000.00"),
                                "Same account test"
                        )
        );


        Account updatedAccount =
                accountRepository
                        .findByAccountNumber(
                                account.getAccountNumber()
                        )
                        .orElseThrow();

        assertEquals(
                new BigDecimal("10000.00"),
                updatedAccount.getBalance()
        );
    }



    @Test
    void shouldRejectTransferWithInsufficientBalance() {

        Role role = roleRepository.findByName("CUSTOMER")
                .orElseThrow();

        User secondUser = new User();

        secondUser.setUsername("insufficientreceiver");
        secondUser.setPassword("test-password");
        secondUser.setRole(role);

        secondUser = userRepository.save(secondUser);


        Customer secondCustomer = new Customer();

        secondCustomer.setUser(secondUser);
        secondCustomer.setFirstName("Amit");
        secondCustomer.setLastName("Kumar");
        secondCustomer.setEmail("insufficient@test.com");
        secondCustomer.setPhone("9876543212");
        secondCustomer.setAddress("Pune");

        secondCustomer = customerRepository.save(secondCustomer);


        AccountType accountType =
                accountTypeRepository
                        .findByName("TEST-SAVINGS")
                        .orElseThrow();


        final Account receiverAccount = new Account();

        receiverAccount.setAccountNumber("555555555555");
        receiverAccount.setCustomer(secondCustomer);
        receiverAccount.setAccountType(accountType);
        receiverAccount.setBalance(
                new BigDecimal("1000.00")
        );
        receiverAccount.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(receiverAccount);


        assertThrows(
                IllegalArgumentException.class,
                () ->
                        transactionService.transfer(
                                account.getAccountNumber(),
                                receiverAccount.getAccountNumber(),
                                new BigDecimal("20000.00"),
                                "Insufficient balance test"
                        )
        );


        Account updatedSender =
                accountRepository
                        .findByAccountNumber(
                                account.getAccountNumber()
                        )
                        .orElseThrow();

        assertEquals(
                new BigDecimal("10000.00"),
                updatedSender.getBalance()
        );
    }
}