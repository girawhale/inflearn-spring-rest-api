package com.restapi.demoinfleanrestapi.configs;

import com.restapi.demoinfleanrestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AccountService accountService; //UserDetailsService

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore() { // 토큰을 저장
        return new InMemoryTokenStore();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception { // 시큐리티 필터를 적용할지 여부
        web.ignoring().mvcMatchers("/docs/index.html");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // static에는 적용 안함
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception { // 스프링 시큐리티 내로 들어온 상태
////        http.authorizeRequests()
////                .mvcMatchers("/docs/index.html").anonymous()
////                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous()
////        ;
//
//        http
//                .anonymous() // 익명 사용자 허용
//                .and()
//
//                .formLogin() // form 인증 사용(로그인 페이지 등을 설정 가능)
//                .and()
//
//                .authorizeRequests()
//                .mvcMatchers(HttpMethod.GET, "/api/**").permitAll() // get요청을 모든 사용자에게 허용
////                .mvcMatchers(HttpMethod.GET, "/api/**").authenticated() // get요청을 로그인된 사용자에게 허용
//                .anyRequest().authenticated() // 나머지는 인증이 필요
//        ;
//    }
}
