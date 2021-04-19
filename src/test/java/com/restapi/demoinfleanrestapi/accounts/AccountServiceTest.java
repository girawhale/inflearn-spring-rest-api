package com.restapi.demoinfleanrestapi.accounts;

import com.restapi.demoinfleanrestapi.common.AppProperties;
import com.restapi.demoinfleanrestapi.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountServiceTest extends BaseTest {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AppProperties appProperties;

    @Test
    public void findByUsername() {
        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(appProperties.getUserUsername());

        // Then
        assertThat(this.passwordEncoder.matches(appProperties.getUserPassword(), userDetails.getPassword())).isTrue();
    }

    @Test
    public void findByUsernameFail() {
        // Expected : 미리 작성하지 않으면 동작하지 않음
        String username = "random@email.com";

        // When : Junit4
//        expectedException.expect(UsernameNotFoundException.class); // 발생 가능한 예외를 미리 작성
//        expectedException.expectMessage(Matchers.containsString(username));

        // When : Junit5
        assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername(username));

    }


}