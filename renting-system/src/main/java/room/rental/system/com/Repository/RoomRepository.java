package room.rental.system.com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import room.rental.system.com.Entity.Room;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room , UUID> {
    Room findRoomByRoomNumber(String roomNumber );
}
