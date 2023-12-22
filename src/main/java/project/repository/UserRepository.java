package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.shoppingCart s WHERE u.email = :email")
    User findWithShoppingCartByEmail(@Param("email")String email);
    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.passwordResetToken s WHERE u.email = :email")
    Optional<User> findWithPasswordResetTokenByEmail(@Param("email")String email);
}
