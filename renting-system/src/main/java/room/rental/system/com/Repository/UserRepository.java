package room.rental.system.com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import room.rental.system.com.Entity.Users;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users , UUID> {
    Users findUsersByEmail(String email);
}
