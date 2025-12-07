import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Désactiver la protection CSRF pour les API si vous l'utilisez
            // Note: C'est souvent nécessaire pour les applications stateless (sans session)
            .csrf(AbstractHttpConfigurer::disable)

            // 2. Définir les règles d'autorisation
            .authorizeHttpRequests(authorize -> authorize
                // Autoriser l'accès public à TOUS les endpoints Actuator (y compris /actuator/prometheus)
                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                
                // Exiger une authentification pour toutes les autres requêtes
                .anyRequest().authenticated() 
            ); 

        return http.build();
    }
}