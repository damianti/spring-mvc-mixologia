/**
 SecurityConfig.java:
 The SecurityConfig class is a configuration class that defines security-related configurations for the application.
 */
package hac.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the password encoder to be used for authentication.
     *
     * @return the configured PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the DaoAuthenticationProvider with the user details service and password encoder.
     *
     * @return the configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the security filter chain with various security settings.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        configureCors(http);
        configureCsrf(http);
        configureAuthorization(http);
        configureFormLogin(http);
        configureLogout(http);
        configureExceptionHandling(http);

        return http.build();
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) for the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureCors(HttpSecurity http) throws Exception {
        http.cors(withDefaults());
    }

    /**
     * Configures Cross-Site Request Forgery (CSRF) protection for the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureCsrf(HttpSecurity http) throws Exception {
        http.csrf(withDefaults());
    }


    /**
     * Configures authorization rules for the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/register", "/css/**", "/assets/**", "/category").permitAll()
                .requestMatchers("/error/**").permitAll()
                .requestMatchers("/items/admin/**").hasRole("ADMIN")
                .requestMatchers("/category/admin/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers("/shared/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()  // Allow any other requests - for errors not unauthorized
        );
    }

    /**
     * Configures form-based login for the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureFormLogin(HttpSecurity http) throws Exception {
        http.formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );
    }

    /**
     * Configures logout settings for the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureLogout(HttpSecurity http) throws Exception {
        http.logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")

        );
    }

    /**
     * Configures exception handling for the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureExceptionHandling(HttpSecurity http) throws Exception {
        http.exceptionHandling(
                (exceptionHandling) -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpServletResponse.SC_FORBIDDEN);
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/error");
                            dispatcher.forward(request, response);
                        })
        );
    }

}
