package com.eascapeco.cinemapr.bo.config;

import com.eascapeco.cinemapr.api.repository.AdminRepository;
import com.eascapeco.cinemapr.bo.security.access.UrlSecurityMetadataSource;
import com.eascapeco.cinemapr.bo.security.common.RestAuthenticationEntryPoint;
import com.eascapeco.cinemapr.bo.security.factory.UrlResourcesMapFactoryBean;
import com.eascapeco.cinemapr.bo.security.filter.RestLoginProcessingFilter;
import com.eascapeco.cinemapr.bo.security.filter.JWTAuthenticationFilter;
import com.eascapeco.cinemapr.bo.security.filter.PermitAllFilter;
import com.eascapeco.cinemapr.bo.security.handler.RestAuthenticationFailureHandler;
import com.eascapeco.cinemapr.bo.security.handler.RestAuthenticationSuccessHandler;
import com.eascapeco.cinemapr.bo.security.provider.RestAuthenticationProvider;
import com.eascapeco.cinemapr.bo.service.security.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] ignoredMatcherPattern = { "/js/**", "/**/favicon.ico" };
    private final String[] permitAllPattern = { "/login", "/", "/js/**" };

    private final AdminRepository adminRepository;

    private final SecurityResourceService securityResourceService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {//authorize.antMatchers("/loginForm").permitAll()

        http
            .httpBasic().disable()
            .authorizeRequests(authorize -> authorize.anyRequest().permitAll());
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(se -> se.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(permitAllFilter(), FilterSecurityInterceptor.class);
        http.exceptionHandling(
            exceptionHandling -> exceptionHandling.authenticationEntryPoint(new RestAuthenticationEntryPoint()));
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(restLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(ignoredMatcherPattern);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(restAuthenticationProvider());
    }

    @Bean
    public RestLoginProcessingFilter restLoginProcessingFilter() throws Exception {
        RestLoginProcessingFilter restLoginProcessingFilter =  new RestLoginProcessingFilter();
        restLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        restLoginProcessingFilter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler());
        restLoginProcessingFilter.setAuthenticationFailureHandler(restAuthenticationFailureHandler());
        return restLoginProcessingFilter;
    }
    @Bean
    public AuthenticationProvider restAuthenticationProvider() {
        return  new RestAuthenticationProvider(adminRepository, passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler restAuthenticationSuccessHandler() {
        return new RestAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler restAuthenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    public PermitAllFilter permitAllFilter() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllPattern);
        permitAllFilter.setAccessDecisionManager(affirmativeBased());
        permitAllFilter.setSecurityMetadataSource(urlSecurityMetadataSource());
        permitAllFilter.setRejectPublicInvocations(false);

        return permitAllFilter;
    }
    @Bean
    public AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();

        return List.of(authenticatedVoter, webExpressionVoter, roleVoter());
    }

    @Bean
    public RoleHierarchyVoter roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
        roleHierarchyVoter.setRolePrefix("ROLE_");
        return roleHierarchyVoter;
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_TEST");
        return roleHierarchy;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlSecurityMetadataSource() throws Exception {
        return new UrlSecurityMetadataSource(urlResourcesMapFactoryBean().getObject(), securityResourceService);
    }

    private UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean();
        urlResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
        return urlResourcesMapFactoryBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
