
/**
 CustomUserDetailsService.java:
 The CustomUserDetailsService class is an implementation of the UserDetailsService interface
 used for loading user details from the UserRepository.
 */
package hac.config;

import hac.models.User;
import hac.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by username (email) from the UserRepository.
     *
     * @param email the email of the user to load
     * @return a UserDetails object representing the user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().split(","))
                    .build();
        } else {
            throw new UsernameNotFoundException("User '" + email + "' not found.");
        }
    }

}
