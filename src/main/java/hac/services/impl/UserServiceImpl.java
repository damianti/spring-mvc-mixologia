/** UserServiceImpl.java: */
package hac.services.impl;

import hac.dto.UserDto;
import hac.models.User;
import hac.repositories.UserRepository;
import hac.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserServiceImpl is an implementation of the UserService interface
 * that provides methods to perform various operations related to users.
 * and is the service in charge of the UserRepository
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @Override
//    public UserDto getUser(Long id) {
//        User user = userRepository.findUserById(id);
//        return convertToDto(user);
//    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // Check if password and confirm password match
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password must match");
        }

        User user = convertToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }


    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findUserById(userDto.getId());
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password must match");
        }
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        // Only update the password if the user wrote something in the password field
        if (!userDto.getPassword().isEmpty())
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> findUserByEmail(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail);
        if (user != null) {
            return Optional.of(convertToDto(user));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> findUserById(Long userId) {
        User user = userRepository.findUserById(userId);
        if (user != null) {
            return Optional.of(convertToDto(user));
        } else {
            return Optional.empty();
        }
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setPassword(null);
        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }

    public UserDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails)principal).getUsername();
            return findUserByEmail(email).orElse(null);
        }

        if (principal instanceof String) {
            String email = (String) principal;
            return findUserByEmail(email).orElse(null);
        }

        return null;
    }

}
