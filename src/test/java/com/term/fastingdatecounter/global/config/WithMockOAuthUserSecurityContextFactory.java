package com.term.fastingdatecounter.global.config;

import com.term.fastingdatecounter.domain.user.domain.User;
import com.term.fastingdatecounter.domain.user.dto.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithMockOAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithMockOAuthUser> {

    private UserDetailsService userDetailsService;

    @Autowired
    public WithMockOAuthUserSecurityContextFactory(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockOAuthUser oAuthUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", oAuthUser.id());
        attributes.put("name", oAuthUser.name());
        attributes.put("email", oAuthUser.email());

        OAuth2User principal = new DefaultOAuth2User(
                List.of(new OAuth2UserAuthority("ROLE_USER", attributes)),
                attributes,
                oAuthUser.name());

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
                principal,
                principal.getAuthorities(),
                "google");

        context.setAuthentication(token);
        return context;
    }
}
