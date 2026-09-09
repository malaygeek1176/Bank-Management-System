package com.bank.management.controller;

import com.bank.management.dto.request.CreateCustomerRequest;
import com.bank.management.dto.response.CustomerResponse;
import com.bank.management.service.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService
    ) {
        this.customerService = customerService;
    }


    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request
    ) {

        CustomerResponse response =
                customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or @customerSecurity.isOwner(#id, authentication)"
    )
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                customerService.getCustomerById(id)
        );
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {

        return ResponseEntity.ok(
                customerService.getAllCustomers()
        );
    }


    @PutMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or @customerSecurity.isOwner(#id, authentication)"
    )
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CreateCustomerRequest request
    ) {

        return ResponseEntity.ok(
                customerService.updateCustomer(id, request)
        );
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id
    ) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }
}