package room.rental.system.com.Controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import room.rental.system.com.Dto.HouseDto;
import room.rental.system.com.Dto.RoomDto;
import room.rental.system.com.Entity.House;
import room.rental.system.com.Entity.Room;
import room.rental.system.com.Entity.Todo;
import room.rental.system.com.Repository.HouseRepository;
import room.rental.system.com.Repository.RoomRepository;
import room.rental.system.com.Repository.TodoRepo;
import room.rental.system.com.Service.CloudinaryImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TestController {

    @Autowired
    TodoRepo todoRepo;

    @Autowired
    CloudinaryImageService cloudinaryImageService;

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    Gson gson;

    @GetMapping("/test")
    public ResponseEntity<Map<String , String>> intro(){
        return ResponseEntity.ok(Map.of("Message" , "Welcome to Room rental System Website !"));
    }

    @PostMapping("/addMessage")
    public ResponseEntity<Map<String ,String>> addMessage(@RequestParam String message){
        Todo todo = new Todo();
        todo.setMessage(message);
        todoRepo.save(todo);
        return ResponseEntity.ok(Map.of("Message", "Added Succesfully"));

    }


    @PostMapping("/addMessageByPath/{message}")
    public ResponseEntity<Map<String ,String>> addMessageByPath(@PathVariable String message){
        Todo todo = new Todo();
        todo.setMessage(message);
        todoRepo.save(todo);
        return ResponseEntity.ok(Map.of("Message", "Added Succesfully"));

    }

    @PostMapping("/addMessageByJson")
    public ResponseEntity<Map<String ,String>> addMessage(@RequestBody Todo message){

        todoRepo.save(message);
        return ResponseEntity.ok(Map.of("Message", "Added Succesfully"));

    }


    @PostMapping("/upload")
    public String uploadFiles(
            @RequestParam Map<String, String> otherParams,
            @RequestParam MultiValueMap<String, MultipartFile> pics
    ) {
        // Processing otherParams
        otherParams.forEach((key, value) -> {
            System.out.println("Param Key: " + key + ", Value: " + value);
        });

        // Processing pics map
        pics.forEach((key, files) -> {
            System.out.println("Key: " + key);
            files.forEach(file -> {
                System.out.println("File Name: " + file.getOriginalFilename());
                System.out.println("File Size: " + file.getSize());
            });
        });

        return "Files uploaded successfully!";
    }


//    @PostMapping("/upload/house")
//    public String uploadFiles(
//            @RequestParam String house,
//            @RequestParam(required = false) List<String> rooms,
//            @RequestParam(required = false) MultiValueMap<String, MultipartFile> pics
//    ) {
//
//        if(!SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("OWNER_ROLE")){
//            return "Unauthorized for house Creation ! ";
//        }
//
//        // Process the house
//        System.out.println("House: " + house);
//        HouseDto house1 = gson.fromJson(house , HouseDto.class);
//        House existHouse = houseRepository.findHouseByHouseName(house1.getHouseName());
//        if(existHouse!=null){
//            return "This House is already exist !";
//        }
//        House newHouse = new House();
//        newHouse.setAddress(house1.getAddress());
//        newHouse.setCity(house1.getCity());
//        newHouse.setHouseName(house1.getHouseName());
//        newHouse.setContact(house1.getContact());
//        newHouse.setState(house1.getState());
//        newHouse.setPincode(house1.getPincode());
//        newHouse.setDescription(house1.getDescription());
//        List<Room> roomList = new ArrayList<>();
//        // Process the rooms
//        if (pics != null && !pics.isEmpty()) {
//            List<MultipartFile> Hpic = pics.get("HouseHeadPic");
//            List<MultipartFile> housePics = pics.get("HousePic");
//            List<String> picUrl = new ArrayList<>();
//            List<String> picUrlPublicId = new ArrayList<>();
//            if (Hpic!=null) {
//
//                Hpic.forEach(r -> {
//                    Map map1 = null;
//                    try {
//                        map1 = this.cloudinaryImageService.upload(r);
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                    String url = (String) map1.get("url");
//                    String picPublicID = (String) map1.get("public_id");
//                    picUrl.add(url);
//                    picUrlPublicId.add(picPublicID);
//                });
//                if (!(picUrl.isEmpty() && picUrlPublicId.isEmpty())) {
//                    newHouse.setHouseHPic(picUrl.get(0));
//                    newHouse.setHouseHPicPublicID(picUrlPublicId.get(0));
//                }
//            }
//            if (housePics != null) {
//                picUrl.clear();
//                picUrlPublicId.clear();
//                housePics.forEach(r -> {
//                    Map map1 = null;
//                    try {
//                        map1 = this.cloudinaryImageService.upload(r);
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                    String url = (String) map1.get("url");
//                    String picPublicID = (String) map1.get("public_id");
//                    picUrl.add(url);
//                    picUrlPublicId.add(picPublicID);
//                });
//                if (!(picUrl.isEmpty() && picUrlPublicId.isEmpty())) {
//                    newHouse.setPics(picUrl);
//                    newHouse.setPicsPublicID(picUrlPublicId);
//                }
//            }
//        }
//        AtomicInteger invalidRoom = new AtomicInteger();
//     House finalHouse =   houseRepository.save(newHouse);
//        if (rooms != null && !rooms.isEmpty()) {
//
//           rooms.stream().forEach(e->{
//               RoomDto room = gson.fromJson(e , RoomDto.class);
//               Room existRoom = roomRepository.findRoomByRoomNumber(room.getRoomNumber());
//               if(existRoom != null){
//                   invalidRoom.getAndIncrement();
//               }
//               Room newRoom = new Room();
//               newRoom.setRoomNumber(room.getRoomNumber());
//               newRoom.setDescription(room.getDescription());
//               newRoom.setRentPrice(room.getRentPrice());
//               newRoom.setHouseName(newHouse.getHouseName());
//
//               if (pics != null && !pics.isEmpty()) {
//                   List<MultipartFile> Hpic = pics.get("RH" + room.getRoomNumber());
//                   List<MultipartFile> roomPics = pics.get("R" + room.getRoomNumber());
//                   List<String> picUrl = new ArrayList<>();
//                   List<String> picUrlPublicId = new ArrayList<>();
//                   if (Hpic!=null) {
//
//                       Hpic.forEach(r -> {
//                           Map map1 = null;
//                           try {
//                               map1 = this.cloudinaryImageService.upload(r);
//                           } catch (IOException ex) {
//                               throw new RuntimeException(ex);
//                           }
//                           String url = (String) map1.get("url");
//                           String picPublicID = (String) map1.get("public_id");
//                           picUrl.add(url);
//                           picUrlPublicId.add(picPublicID);
//                       });
//                       if (!(picUrl.isEmpty() && picUrlPublicId.isEmpty())) {
//                           newRoom.setRoomHPic(picUrl.get(0));
//                           newRoom.setRoomHPicpublicID(picUrlPublicId.get(0));
//                       }
//                   }
//                   if (roomPics != null) {
//                       picUrl.clear();
//                       picUrlPublicId.clear();
//                       roomPics.forEach(r -> {
//                           Map map1 = null;
//                           try {
//                               map1 = this.cloudinaryImageService.upload(r);
//                           } catch (IOException ex) {
//                               throw new RuntimeException(ex);
//                           }
//                           String url = (String) map1.get("url");
//                           String picPublicID = (String) map1.get("public_id");
//                           picUrl.add(url);
//                           picUrlPublicId.add(picPublicID);
//                       });
//                   if (!(picUrl.isEmpty() && picUrlPublicId.isEmpty())) {
//                       newRoom.setRoomPics(picUrl);
//                       newRoom.setRoomPicsPublicID(picUrlPublicId);
//                   }
//                   }
//               }
//               newRoom.setHouse(finalHouse);
//               roomList.add(newRoom);
//           });
//        } else {
//            System.out.println("No rooms provided.");
//        }
//
//
//       List<Room> newRooms = roomRepository.saveAll(roomList);
//
//        if(invalidRoom.get()>0){
//            return "House are added Successfully But some rooms are remove because room number is alread exist ";
//        }
//        return "Upload successful!";
//    }
//
//    @GetMapping("/getAllRooms")
//    public List<House> getAllhouse(){
//        return houseRepository.findAll();
//    }
}
