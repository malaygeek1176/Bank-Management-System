package com.bank.management.service;

import com.bank.management.dto.request.CreateCustomerRequest;
import com.bank.management.dto.response.CustomerResponse;
import com.bank.management.entity.Customer;
import com.bank.management.exception.DuplicateResourceException;
import com.bank.management.exception.ResourceNotFoundException;
import com.bank.management.repository.CustomerRepository;
import com.bank.management.repository.RoleRepository;
import com.bank.management.repository.UserRepository;
import com.bank.management.service.impl.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;


    @Test
    void getCustomerById_shouldReturnCustomer() {

        Customer customer = new Customer();

        customer.setFirstName("Malay");
        customer.setLastName("Yadav");
        customer.setEmail("malay@gmail.com");
        customer.setPhone("9876543210");
        customer.setAddress("Bhopal");

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        CustomerResponse response =
                customerService.getCustomerById(1L);

        assertNotNull(response);
        assertEquals("Malay", response.getFirstName());
        assertEquals("Yadav", response.getLastName());
        assertEquals("malay@gmail.com", response.getEmail());
        assertEquals("9876543210", response.getPhone());

        verify(customerRepository).findById(1L);
    }


    @Test
    void getCustomerById_shouldThrowExceptionWhenCustomerNotFound() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> customerService.getCustomerById(1L)
        );

        verify(customerRepository).findById(1L);
    }


    @Test
    void getAllCustomers_shouldReturnCustomers() {

        Customer customer1 = new Customer();
        customer1.setFirstName("Malay");
        customer1.setLastName("Yadav");
        customer1.setEmail("malay@gmail.com");
        customer1.setPhone("9876543210");
        customer1.setAddress("Bhopal");

        Customer customer2 = new Customer();
        customer2.setFirstName("Rahul");
        customer2.setLastName("Sharma");
        customer2.setEmail("rahul@gmail.com");
        customer2.setPhone("9876543211");
        customer2.setAddress("Delhi");

        when(customerRepository.findAll())
                .thenReturn(List.of(customer1, customer2));

        List<CustomerResponse> responses =
                customerService.getAllCustomers();

        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals("Malay", responses.get(0).getFirstName());
        assertEquals("Rahul", responses.get(1).getFirstName());

        verify(customerRepository).findAll();
    }


    @Test
    void deleteCustomer_shouldDeleteCustomer() {

        Customer customer = new Customer();

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository).delete(customer);
    }


    @Test
    void deleteCustomer_shouldThrowExceptionWhenCustomerNotFound() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> customerService.deleteCustomer(1L)
        );

        verify(customerRepository, never()).delete(any(Customer.class));
    }


    @Test
    void updateCustomer_shouldUpdateCustomer() {

        Customer customer = new Customer();

        customer.setFirstName("Old");
        customer.setLastName("Name");
        customer.setEmail("old@gmail.com");
        customer.setPhone("9876543210");
        customer.setAddress("Old Address");

        CreateCustomerRequest request =
                new CreateCustomerRequest();

        request.setFirstName("New");
        request.setLastName("Name");
        request.setAddress("New Address");

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(customerRepository.save(customer))
                .thenReturn(customer);

        CustomerResponse response =
                customerService.updateCustomer(1L, request);

        assertNotNull(response);

        assertEquals("New", response.getFirstName());
        assertEquals("Name", response.getLastName());
        assertEquals("New Address", response.getAddress());

        verify(customerRepository).findById(1L);
        verify(customerRepository).save(customer);
    }


    @Test
    void updateCustomer_shouldThrowExceptionWhenCustomerNotFound() {

        CreateCustomerRequest request =
                new CreateCustomerRequest();

        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> customerService.updateCustomer(1L, request)
        );

        verify(customerRepository, never())
                .save(any(Customer.class));
    }


    @Test
    void createCustomer_shouldThrowExceptionWhenEmailAlreadyExists() {

        CreateCustomerRequest request =
                new CreateCustomerRequest();

        request.setEmail("existing@gmail.com");
        request.setPhone("9876543210");

        when(customerRepository.existsByEmail(
                "existing@gmail.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> customerService.createCustomer(request)
        );

        verify(customerRepository)
                .existsByEmail("existing@gmail.com");

        verify(customerRepository, never())
                .save(any(Customer.class));
    }


    @Test
    void createCustomer_shouldThrowExceptionWhenPhoneAlreadyExists() {

        CreateCustomerRequest request =
                new CreateCustomerRequest();

        request.setEmail("new@gmail.com");
        request.setPhone("9876543210");

        when(customerRepository.existsByEmail(
                "new@gmail.com"))
                .thenReturn(false);

        when(customerRepository.existsByPhone(
                "9876543210"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> customerService.createCustomer(request)
        );

        verify(customerRepository)
                .existsByPhone("9876543210");

        verify(customerRepository, never())
                .save(any(Customer.class));
    }
}