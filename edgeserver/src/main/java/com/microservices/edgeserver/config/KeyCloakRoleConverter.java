package com.microservices.edgeserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyCloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String,Object> map1 = (Map<String, Object>) source.getClaims().get("resource_access");

        if (map1 == null || map1.isEmpty()){
            return new ArrayList<>();
        }

        Map<String,Object> myAuthorities = (Map<String, Object>) map1.get("eazy-bank-microservice");

        Collection<String> roles =
                (Collection<String>) myAuthorities.get("roles");

        Collection<GrantedAuthority> myGrantedAuthority =  roles.stream().
                map( x -> "ROLE_" + x ).map( SimpleGrantedAuthority::new ).collect(Collectors.toList());

        return myGrantedAuthority;
    }

}
