package com.restapi.demoinfleanrestapi.accounts;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
// 로그인 되지 않은 사용자의 경우 문제가 발생
// getAccount를 사용하고 싶지만 'anonymousUser'라는 문자열이 반환되기 때문에 제대로 동작하지 않는다
// principal이 그냥 문자열!
public @interface CurrentUser {
}
