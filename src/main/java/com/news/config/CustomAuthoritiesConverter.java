package com.news.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        log.debug("Converting Jwt {}", jwt);

        // Извлекаем роли из поля realm_access
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");

        log.debug("Roles {}", roles);
        List<GrantedAuthority> authorityList = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        log.debug("Authorities {}", authorityList);
        return authorityList;
    }
}