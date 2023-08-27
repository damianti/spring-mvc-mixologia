/**
 * UserRepository.java
 *
 */
package hac.repositories;

import hac.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * Finds a user by ID.
     *
     * @param id The ID of the user.
     * @return The user with the specified ID.
     */
    User findUserById(long id);

    /**
     * Finds a user by email.
     *
     * @param email The email of the user.
     * @return The user with the specified email.
     */
    User findUserByEmail(String email);
//    List<User> findByUsername(String name);
//    User findUserByUsername(String name);
//    List<User> findByEmail(String email);
//    List<User> findByUsernameAndEmail(String name, String email);
//    List<User> findFirst10ByOrderByUsernameDesc(); // find first 10 users ordered by name desc
}
