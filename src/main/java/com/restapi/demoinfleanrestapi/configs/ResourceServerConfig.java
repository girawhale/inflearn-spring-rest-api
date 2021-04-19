package com.restapi.demoinfleanrestapi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter { // 토큰 기반으로 인증정보가 있는지 확인하고, 접근을 제한
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception { // 리소스의 id를 설정
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()
                .and()

                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**")
//                .anonymous() // 로그인 하지 않은 사용자만 가능
                .permitAll() // 모든 사용자

                .anyRequest()
                .authenticated()
                .and()

                .exceptionHandling() // 인증이 안되거나 권한이 없는 경우
                .accessDeniedHandler(new OAuth2AccessDeniedHandler()) // 403을 던져줌
        ;
    }
}
