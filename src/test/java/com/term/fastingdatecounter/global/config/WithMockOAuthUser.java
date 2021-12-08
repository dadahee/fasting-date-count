package com.term.fastingdatecounter.global.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockOAuthUserSecurityContextFactory.class)
public @interface WithMockOAuthUser {
    long id() default 123L;
    String email() default "test-email@test.com";
    String name() default "test-user";
}
