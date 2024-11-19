package room.rental.system.com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import room.rental.system.com.Entity.House;

import java.util.UUID;

public interface HouseRepository extends JpaRepository<House , UUID> {
    House findHouseByHouseName(String houseName);
}
