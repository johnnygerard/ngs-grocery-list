package dev.jgerard.ngsgrocerylist;

import dev.jgerard.ngsgrocerylist.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
