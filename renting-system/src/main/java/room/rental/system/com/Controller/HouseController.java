package room.rental.system.com.Controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import room.rental.system.com.Dto.HouseDto;
import room.rental.system.com.Dto.RoomDto;
import room.rental.system.com.Entity.House;
import room.rental.system.com.Entity.Room;
import room.rental.system.com.Repository.HouseRepository;
import room.rental.system.com.Repository.RoomRepository;
import room.rental.system.com.Service.CloudinaryImageService;
import room.rental.system.com.Service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HouseController {

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    Gson gson;

    @Autowired
    CloudinaryImageService cloudinaryImageService;
    @Autowired
    UserService userService;

    @PostMapping("/upload/house")
    public ResponseEntity<Map<String , String>> uploadFiles(
            @RequestParam String house,
            @RequestParam(required = false) List<String> rooms,
            @RequestParam(required = false) MultiValueMap<String, MultipartFile> pics
    ) {

        if(!SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("OWNER_ROLE")){
            return ResponseEntity.ok(Map.of("Message" ,"Unauthorized for house Creation ! "));
        }
        return ResponseEntity.ok(Map.of("Message" ,userService.createHouose(house, rooms, pics)));

    }

    @GetMapping("/getAllHouse")
    public List<House> getAllhouse(@RequestParam String status){
        if(status!=null){
            if(status.equalsIgnoreCase("RemainRoom")){
                return houseRepository.findHousesWithRoomsLessThanBookedRoom();
            }
            else {
                return houseRepository.findHousesWithRoomsEqualToBookedRoom();
            }
        }
        return houseRepository.findAll();
    }
    @GetMapping("/getAllRoom")
    public List<Room> getAllRoom(@RequestParam(required = false) String isBooked){
        if(isBooked !=null){
            if(isBooked.equalsIgnoreCase("true")){
                return roomRepository.findRoomByAvailabilityStatus(true);
            }
            return roomRepository.findRoomByAvailabilityStatus(false);
        }
        return roomRepository.findAll();
    }

    @PostMapping ("/delProfilePicOfHouse")
    public ResponseEntity<Map<String , String>> delProfilePicOfHouseOrRoom(@RequestParam boolean isHouse , @RequestParam(required = false) String url  , @RequestParam(required = false) String roomNumber , @RequestParam(required = false) String housNmae, @RequestParam(required = false) MultipartFile updatePic ){
        if(!SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("OWNER_ROLE")){
            return ResponseEntity.ok(Map.of("Message" ,"Unauthorized for house Creation ! "));
        }
        if(isHouse && housNmae !=null&&url!=null){

            House house =    houseRepository.findHouseByHouseName(housNmae);
            if(house != null){

            }

            if (updatePic!=null){
                List<String> picsData  = new ArrayList<>();
                upadatPic(updatePic ,picsData);
                if (!picsData.isEmpty() && picsData.size() == 2) {
                    house.setHouseHPic(picsData.get(0));
                    house.setHouseHPicPublicID(picsData.get(1));
                }
            }
            else {
            house.setHouseHPic(null);
            house.setHouseHPicPublicID(null);
            houseRepository.save(house);
            }
            return ResponseEntity.ok(Map.of("Message" ,"House Profile pic update successfully"));
        }
        else if (!isHouse && housNmae != null && roomNumber != null && url!=null){

            Room room = roomRepository.findRoomByRoomNumberAndHouseName(roomNumber ,housNmae);
            if(room!=null){

            }
            if (updatePic!=null){
                List<String> picsData  = new ArrayList<>();
                upadatPic(updatePic ,picsData);
                if (!picsData.isEmpty() && picsData.size() == 2) {
                    room.setRoomHPic(picsData.get(0));
                    room.setRoomHPicpublicID(picsData.get(1));
                }
            }
            else {
                room.setRoomHPic(null);
                room.setRoomHPicpublicID(null);
                roomRepository.save(room);
            }
            return ResponseEntity.ok(Map.of("Message" ,"Room Profile pic update successfully"));
        }

        return ResponseEntity.ok(Map.of("Message" ,"Invalid param for deletion of pic "));
    }

    private void upadatPic(MultipartFile updatePic ,List<String> picsData ) {
        try {
            Map map1 = this.cloudinaryImageService.upload(updatePic);

            picsData.add((String) map1.get("url"));
            picsData.add((String) map1.get("public_id"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
