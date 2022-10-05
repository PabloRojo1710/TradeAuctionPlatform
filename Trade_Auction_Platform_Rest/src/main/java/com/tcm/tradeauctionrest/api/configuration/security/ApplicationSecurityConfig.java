package com.tcm.tradeauctionrest.api.configuration.security;

import javax.sql.DataSource;

import com.tcm.tradeauctionrest.api.configuration.security.jwt.JwtConfig;
import com.tcm.tradeauctionrest.api.configuration.security.jwt.JwtTokenVerifierFilter;
import com.tcm.tradeauctionrest.api.configuration.security.jwt.JwtUsernamePasswordAuthenticationFilter;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USERS_QUERY = "SELECT username AS principal, " + "password AS credentials, "
            + "enabled " + "FROM profile WHERE username = ?";

    private static final String AUTHORITIES_QUERY = "SELECT username AS principal, role FROM authorities WHERE username = ?";

    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private DataSource dataSource;

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, DataSource dataSource, JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtConfig))
                .addFilterAfter(new JwtTokenVerifierFilter(jwtConfig), JwtUsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/", "index", "/css/*", "/js/*", "/profiles", "/platform-comission")
                .permitAll().antMatchers("/profiles/me", "/profiles/*/euros/*", "/profiles/*/auctions/*/bids")
                .hasAnyRole("ADMIN", "BROKER", "BIDDER")
                .antMatchers("/bitcoins/price", "/profiles/*/bitcoins/*", "/profiles/*/purchases", "/profiles/*/auctions",
                        "/profiles/*/auctions")
                .hasAnyRole("ADMIN", "BROKER").antMatchers("/auctions", "/profiles/*/bids").hasAnyRole("ADMIN", "BIDDER")
                .antMatchers("/comissions").hasRole("ADMIN").anyRequest().authenticated();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(USERS_QUERY)
                .authoritiesByUsernameQuery(AUTHORITIES_QUERY).passwordEncoder(passwordEncoder);
    }
}
