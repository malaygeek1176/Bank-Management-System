package com.bank.management.service;

import com.bank.management.dto.request.CreateCustomerRequest;
import com.bank.management.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(
            CreateCustomerRequest request
    );

    CustomerResponse getCustomerById(Long id);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(
            Long id,
            CreateCustomerRequest request
    );

    void deleteCustomer(Long id);
}