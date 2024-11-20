package room.rental.system.com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import room.rental.system.com.Entity.House;

import java.util.List;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<House , UUID> {
    House findHouseByHouseName(String houseName);
    @Query("SELECT h FROM House h WHERE SIZE(h.rooms) < h.bookedRoom")
    List<House> findHousesWithRoomsLessThanBookedRoom();

    @Query("SELECT h FROM House h WHERE SIZE(h.rooms) = h.bookedRoom")
    List<House> findHousesWithRoomsEqualToBookedRoom();




}
