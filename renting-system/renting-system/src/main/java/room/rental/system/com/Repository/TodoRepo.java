package room.rental.system.com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import room.rental.system.com.Entity.Todo;

@Repository
public interface TodoRepo extends JpaRepository<Todo ,Long> {
}
