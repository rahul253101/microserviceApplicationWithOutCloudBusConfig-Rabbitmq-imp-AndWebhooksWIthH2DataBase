package com.eazybytes.loans.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("loans")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ContactInfoDto{
    String message;
    Map<String,String> contactDetails;
    List<String> onCallSupport;
}
