package tacos.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tacos.domain.User;
import tacos.persistence.UserRepository;

import java.util.Optional;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsManager(final UserRepository userRepository) {
        return username -> {
            final Optional<User> user = userRepository.findUserByUsername(username);
            if (user.isPresent()) {
                return user.get();
            }

            throw new UsernameNotFoundException(String.format("User %s not found", username));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        final HttpSecurity urlPermissions = http.authorizeRequests()
                        .antMatchers("/design", "/orders")
                        .hasRole("USER")
                        .antMatchers("/", "/**")
                        .permitAll()
                        .and();

        final HttpSecurity authentication = urlPermissions.formLogin()
                .loginPage("/login")
                // specify URL which Spring Security should listen, to handle login submissions
                .loginProcessingUrl("/authenticate")
                // change default username expected field name on login page
                .usernameParameter("usr")
                // change default password expected field name on login page
                .passwordParameter("pwd")
                // specify URL redirect to, when user successfully authenticated. 2-nd parameter forces redirection
                .defaultSuccessUrl("/design", true)
                .and()
                .logout()
                // once user is logged out redirect on login page
                .logoutSuccessUrl("/login")
                .and();

        return authentication.build();
    }
}
