/** UserService.java:*/
package hac.services;

import hac.dto.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserService {

//    UserDto getUser(Long id);
    List<UserDto> getAllUsers();
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUser(Long id);

    Optional<UserDto> findUserByEmail(String userEmail);

    Optional<UserDto> findUserById(Long userId);

    UserDto getAuthenticatedUser() ;
}
