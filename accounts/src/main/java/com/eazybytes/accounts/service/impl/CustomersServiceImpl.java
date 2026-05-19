package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.dto.CardsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.CustomerServiceDto;
import com.eazybytes.accounts.dto.LoansDto;
import com.eazybytes.accounts.service.ICustomersService;
import com.eazybytes.accounts.service.client.CardsFeignClient;
import com.eazybytes.accounts.service.client.LoansFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class CustomersServiceImpl implements ICustomersService {
    @Autowired
    CardsFeignClient cardsFeignClient;
    @Autowired
    LoansFeignClient loansFeignClient;
    @Autowired
    AccountsServiceImpl accountsService;
    @Override
    public CustomerServiceDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        ResponseEntity<CardsDto>  cardResponse = cardsFeignClient.fetchCardDetails(mobileNumber, correlationId);
        ResponseEntity<LoansDto>  loansResponse = loansFeignClient.fetchLoanDetails(mobileNumber,correlationId);
        CustomerServiceDto customerServiceDto = new CustomerServiceDto();

        if(cardResponse != null){
            CardsDto cardsDto = cardResponse.getBody();
            customerServiceDto.setCardsDto(cardsDto);
        }
        if(loansResponse != null){
            LoansDto loansDto = loansResponse.getBody();
            customerServiceDto.setLoansDto(loansDto);
        }

        CustomerDto customerDto = accountsService.fetchAccount(mobileNumber);
        customerServiceDto.setAccountsDto(customerDto.getAccountsDto());

        customerServiceDto.setUserName(customerDto.getName());
        customerServiceDto.setUserEmail(customerDto.getEmail());
        customerServiceDto.setMobileNumber(customerDto.getMobileNumber());

        return customerServiceDto;

    }
}
