package com.bank.management.service.impl;

import com.bank.management.dto.request.CreateCustomerRequest;
import com.bank.management.dto.response.CustomerResponse;
import com.bank.management.entity.Customer;
import com.bank.management.exception.DuplicateResourceException;
import com.bank.management.exception.ResourceNotFoundException;
import com.bank.management.repository.CustomerRepository;
import com.bank.management.service.CustomerService;
import org.springframework.stereotype.Service;

import com.bank.management.entity.Role;
import com.bank.management.entity.User;
import com.bank.management.repository.RoleRepository;
import com.bank.management.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public CustomerResponse createCustomer(
            CreateCustomerRequest request) {

        if (customerRepository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists: "
                            + request.getEmail()
            );
        }

        if (customerRepository.existsByPhone(
                request.getPhone())) {

            throw new DuplicateResourceException(
                    "Phone already exists: "
                            + request.getPhone()
            );
        }

        // Find CUSTOMER role
        Role customerRole =
                roleRepository.findByName("CUSTOMER")
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "CUSTOMER role not found"
                                ));

        // Create User
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(customerRole);

        User savedUser =
                userRepository.save(user);

        // Create Customer
        Customer customer = new Customer();

        customer.setUser(savedUser);
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        Customer savedCustomer =
                customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id
                                ));

        return mapToResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CustomerResponse updateCustomer(
            Long id,
            CreateCustomerRequest request) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id
                                ));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setAddress(request.getAddress());

        Customer updatedCustomer =
                customerRepository.save(customer);

        return mapToResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id
                                ));

        customerRepository.delete(customer);
    }

    private CustomerResponse mapToResponse(
            Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress()
        );
    }
}
