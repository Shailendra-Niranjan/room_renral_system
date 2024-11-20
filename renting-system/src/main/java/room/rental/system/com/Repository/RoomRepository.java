package room.rental.system.com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import room.rental.system.com.Entity.Room;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room , UUID> {
    @Query("SELECT r FROM Room r WHERE r.roomNumber = :roomNumber AND r.houseName = :houseName")
    Room findRoomByRoomNumberAndHouseName(@Param("roomNumber") String roomNumber, @Param("houseName") String houseName);

    List<Room> findRoomByAvailabilityStatus(boolean isbooked);
}
