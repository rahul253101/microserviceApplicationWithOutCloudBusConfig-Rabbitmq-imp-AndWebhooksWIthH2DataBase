package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.dto.CustomerServiceDto;
import com.eazybytes.accounts.service.ICustomersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Accounts in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE account details"
)
@Validated
@RefreshScope
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomersController {

    @Autowired
    ICustomersService customerService;
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerServiceDto> fetchCustomerDetails(@Valid @RequestParam
                                                                   @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber,
                                                                   @RequestHeader("eazybank-correlation-id") String correlationId){


        return ResponseEntity.status(HttpStatus.OK).body(customerService.fetchCustomerDetails(mobileNumber,correlationId));

    }
}
