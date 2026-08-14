package com.bank.management.service.impl;

import com.bank.management.entity.Customer;
import com.bank.management.exception.DuplicateResourceException;
import com.bank.management.exception.ResourceNotFoundException;
import com.bank.management.repository.CustomerRepository;
import com.bank.management.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

//Spring ko batata hai CustomerServiceImpl ko Spring Bean ke roop me manage karo
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    //Dependency Injection
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {

        //Duplicate email check
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists: " + customer.getEmail()
            );
        }

        //Duplicate phone check
        if (customerRepository.existsByPhone(customer.getPhone())) {
            throw new DuplicateResourceException(
                    "Phone already exists: " + customer.getPhone()
            );
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {

        //Customer not found agar ID nhi mili
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id: " + id
                        )
                );
    }

    @Override
    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {

        Customer existingCustomer = getCustomerById(id);

        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setDateOfBirth(customer.getDateOfBirth());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {

        Customer existingCustomer = getCustomerById(id);

        customerRepository.delete(existingCustomer);
    }
}

//flow
//Database
//   ↓
//Existing Customer
//   ↓
//Modify fields
//   ↓
//save()
//   ↓
//Updated record

