package inflearn.practice.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// TODO : SpringBoot 3.x 이후부터 왜 WebSecurityConfigureAdapter 를 상속받지 않는가?! 정리
	// TODO : AuthenticationManager + UserDetailsService 정리!

	@Bean
	public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http.csrf(CsrfConfigurer::disable);
		http.authorizeHttpRequests(authorize ->
			authorize.requestMatchers("/", "/info", "/account/**").permitAll()
					 .requestMatchers("/admin").hasRole("ADMIN")
					 .anyRequest().authenticated()
		);
		http.formLogin(Customizer.withDefaults());
		http.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Default : Bcrypt
	}

	@Profile("in-memory")
	@Bean
	@DependsOn("userDetailsService")
	public AuthenticationManager authenticationManager(@Autowired final UserDetailsService userDetailsService) {
		final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

		return new ProviderManager(authProvider);
	}

	@Profile("in-memory")
	@Bean
	public UserDetailsService userDetailsService() {
		final UserDetails user = User.withUsername("doblee")
									 .password("{noop}123")
									 .roles("USER")
									 .build();

		final UserDetails admin = User.withUsername("root")
									  .password("{noop}123")
									  .roles("ADMIN")
									  .build();

		return new InMemoryUserDetailsManager(user, admin);
	}

}
