package com.term.fastingdatecounter.config;

import com.term.fastingdatecounter.api.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위함
                .and()
                    .authorizeRequests() // endpoint별 권한 관리 시작점
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/deploy").permitAll() // antMatchers를 통해 권한 관리 대상 지정, permitAll: 전체 열람 가능
                    .anyRequest().authenticated() // 나머지 요청에 대해서는 로그인한 사용자만 허용
                .and()
                    .logout()
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동 경로
                        .invalidateHttpSession(true) // 로그아웃 이후 세션 전체 삭제
                        .deleteCookies("JSESSIONID") // 쿠키도 삭제
                .and()
                    .oauth2Login() // 로그인 기능에 대한 진입점
                        .userInfoEndpoint() // 로그인 성공 이후 사용자 정보를 가져올 때 설정
                            .userService(customOAuth2UserService) // 로그인 성공 시 조치를 진행할 서비스 구현체
                            .and()
                        .defaultSuccessUrl("/food", true);
    }
}
