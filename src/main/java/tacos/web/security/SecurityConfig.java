package tacos.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(final PasswordEncoder passwordEncoder) {
        final List<UserDetails> users = new ArrayList<>();
        users.add(new User(
                "buzz",
                passwordEncoder.encode("password"),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        ));

        users.add(new User(
                "woody",
                passwordEncoder.encode("password"),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        ));

        return new InMemoryUserDetailsManager(users);
    }
}
