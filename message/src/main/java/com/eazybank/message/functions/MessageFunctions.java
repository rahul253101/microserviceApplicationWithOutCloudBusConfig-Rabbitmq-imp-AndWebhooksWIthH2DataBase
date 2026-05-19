package com.eazybank.message.functions;

import com.eazybank.message.dtos.AccountsMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunctions {

    Logger logger = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    Function<AccountsMsgDto, AccountsMsgDto> email(){
        return accountsMsgDto -> {
            logger.info("Sending email with the details: " + accountsMsgDto.toString());
            return accountsMsgDto;
        };
    }

    @Bean
    Function<AccountsMsgDto, Long> sms(){
        return accountsMsgDto -> {
            logger.info("Sending sms with the details: " + accountsMsgDto.toString());
            return accountsMsgDto.accountNumber();
        };
    }
}
