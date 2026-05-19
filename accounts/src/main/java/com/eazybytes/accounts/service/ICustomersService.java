package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerServiceDto;

import java.util.Optional;

public interface ICustomersService {

    public CustomerServiceDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
