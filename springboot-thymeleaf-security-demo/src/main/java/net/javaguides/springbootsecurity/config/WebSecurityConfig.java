package net.javaguides.springbootsecurity.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, proxyTargetClass = true, prePostEnabled = false)
public class WebSecurityConfig {

	@Autowired
	private UserDetailsService customUserDetailsService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(customUserDetailsService)
				.passwordEncoder(passwordEncoder);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.headers(headers -> headers
						.frameOptions().sameOrigin())
				.authorizeHttpRequests(requests -> requests
						.requestMatchers("/resources/**", "/webjars/**", "/assets/**").permitAll()
						.requestMatchers("/").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				.formLogin(login -> login
						.loginPage("/login")
						.defaultSuccessUrl("/home")
						.failureUrl("/login?error")
						.permitAll())
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout")
						.deleteCookies("my-remember-me-cookie")
						.permitAll())
				.rememberMe(me -> me
						// .key("my-secure-key")
						.rememberMeCookieName("my-remember-me-cookie")
						.tokenRepository(persistentTokenRepository())
						.tokenValiditySeconds(24 * 60 * 60));
		return http.build();
	}

	PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		tokenRepositoryImpl.setDataSource(dataSource);
		return tokenRepositoryImpl;
	}
}
