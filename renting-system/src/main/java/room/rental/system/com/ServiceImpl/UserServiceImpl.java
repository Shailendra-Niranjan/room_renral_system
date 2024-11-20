package room.rental.system.com.ServiceImpl;


import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import room.rental.system.com.Dto.HouseDto;
import room.rental.system.com.Dto.RoomDto;
import room.rental.system.com.Dto.UserDto;
import room.rental.system.com.Entity.House;
import room.rental.system.com.Entity.Role;
import room.rental.system.com.Entity.Room;
import room.rental.system.com.Entity.Users;
import room.rental.system.com.Repository.HouseRepository;
import room.rental.system.com.Repository.RoomRepository;
import room.rental.system.com.Repository.UserRepository;
import room.rental.system.com.Security.JWTService;
import room.rental.system.com.Security.LoggedInUser;
import room.rental.system.com.Security.UserDeatilsServices;
import room.rental.system.com.Service.CloudinaryImageService;
import room.rental.system.com.Service.EmailService;
import room.rental.system.com.Service.UserService;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceImpl implements UserService {

    private BCryptPasswordEncoder encoder  = new BCryptPasswordEncoder(11);

    // Character pool for the password
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "!@#$%^&*()-_+=<>?";

    private static final SecureRandom RANDOM = new SecureRandom();


    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtService ;
    @Autowired
    private UserDeatilsServices userDeatilsServices;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    Gson gson;

    @Autowired
    CloudinaryImageService cloudinaryImageService;

    @Override
    public String registerUser(UserDto userDto) {
        Users usersExist = userRepository.findUsersByEmail(userDto.getEmail());
        if (usersExist!=null){
            return "User already Exist !";
        }
        Users users = new Users();
        users.setEmail(userDto.getEmail());
        users.setPassword(encoder.encode(userDto.getPassword()));
        users.setAddress(userDto.getAddress());
        users.setContact(userDto.getContact());
        users.setName(userDto.getName());
        users.setCity(userDto.getCity());
        users.setState(userDto.getState());
        users.setPincode(userDto.getPincode());
        if(userDto.getRole().equalsIgnoreCase("owner")){
            users.setRole(Role.OWNER_ROLE);
        }
        else {
            users.setRole(Role.USER_ROLE);
        }
        try {
            emailService.userCreationMail(users.getEmail() ,"Account Created" , userDto.getPassword() ,users.getName());
            userRepository.save(users);

        } catch (IOException e) {
            System.out.println(e);
            return "IOException occur while sending mail";
        } catch (MessagingException e) {
            System.out.println(e);
            return "MessagingException occur while sending mail";
        }catch (Exception e){
            return e.getLocalizedMessage();
        }
        return "Register Successfully !";
    }

    @Override
    public String loginUser(UserDto dto) throws BadRequestException {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        System.out.println(authentication.getAuthorities().toString());
        String token = jwtService.generateToken(dto.getEmail() , String.valueOf(authentication.getAuthorities().stream().toList().get(0)));
        System.out.println(token);
        return token;
    }

    @Override
    public String forgetUserPassword(UserDto userDto) {

      LoggedInUser loggedInUser  = (LoggedInUser) userDeatilsServices.loadUserByUsername(userDto.getEmail());

      String newPassword = generatePassword();
      Users users = loggedInUser.getUsers();
      users.setPassword(encoder.encode(newPassword));
      userRepository.save(users);
        try {
            emailService.sendResetPassword(users.getEmail() ,"Forget Password" , newPassword , users.getName());
        } catch (IOException e) {
            System.out.println(e);
            return "IOException occur while sending mail";
        } catch (MessagingException e) {
            System.out.println(e);
            return "MessagingException occur while sending mail";
        }
        return "New Password sent to your email";
    }



    public static String generatePassword() {
        StringBuilder password = new StringBuilder(10); // Fixed length: 10

        // Generate 10 random characters from the pool
        for (int i = 0; i < 10; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }

    @Override
    public   String createHouose(String house, List<String> rooms, MultiValueMap<String, MultipartFile> pics) {
        // Process the house
        System.out.println("House: " + house);
        HouseDto house1 = gson.fromJson(house, HouseDto.class);
        House existHouse = houseRepository.findHouseByHouseName(house1.getHouseName());
        if(existHouse!=null){
            return "This House is already exist !";
        }
        House newHouse = new House();
        newHouse.setAddress(house1.getAddress());
        newHouse.setCity(house1.getCity());
        newHouse.setHouseName(house1.getHouseName());
        newHouse.setContact(house1.getContact());
        newHouse.setState(house1.getState());
        newHouse.setPincode(house1.getPincode());
        newHouse.setDescription(house1.getDescription());
        List<Room> roomList = new ArrayList<>();
        // Process the rooms
        if (pics != null && !pics.isEmpty()) {
            List<MultipartFile> Hpic = pics.get("HouseHeadPic");
            List<MultipartFile> housePics = pics.get("HousePic");
            List<String> picUrl = new ArrayList<>();
            List<String> picUrlPublicId = new ArrayList<>();
            if (Hpic!=null) {

                Hpic.forEach(r -> {
                    Map map1 = null;
                    try {
                        map1 = this.cloudinaryImageService.upload(r);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String url = (String) map1.get("url");
                    String picPublicID = (String) map1.get("public_id");
                    picUrl.add(url);
                    picUrlPublicId.add(picPublicID);
                });
                if (!(picUrl.isEmpty() && picUrlPublicId.isEmpty())) {
                    newHouse.setHouseHPic(picUrl.get(0));
                    newHouse.setHouseHPicPublicID(picUrlPublicId.get(0));
                }
            }
            if (housePics != null) {
                picUrl.clear();
                picUrlPublicId.clear();
                housePics.forEach(r -> {
                    Map map1 = null;
                    try {
                        map1 = this.cloudinaryImageService.upload(r);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String url = (String) map1.get("url");
                    String picPublicID = (String) map1.get("public_id");
                    picUrl.add(url);
                    picUrlPublicId.add(picPublicID);
                });
                if (!(picUrl.isEmpty() && picUrlPublicId.isEmpty())) {
                    newHouse.setPics(picUrl);
                    newHouse.setPicsPublicID(picUrlPublicId);
                }
            }
        }
        AtomicInteger invalidRoom = new AtomicInteger();
        House finalHouse =   houseRepository.save(newHouse);
        if (rooms != null && !rooms.isEmpty()) {

            rooms.stream().forEach(e->{
                RoomDto room = gson.fromJson(e , RoomDto.class);
                Room existRoom = roomRepository.findRoomByRoomNumberAndHouseName(room.getRoomNumber() , finalHouse.getHouseName());
                if(existRoom != null){
                    invalidRoom.getAndIncrement();
                }
                Room newRoom = new Room();
                newRoom.setRoomNumber(room.getRoomNumber());
                newRoom.setDescription(room.getDescription());
                newRoom.setRentPrice(room.getRentPrice());
                newRoom.setHouseName(newHouse.getHouseName());

                if (pics != null && !pics.isEmpty()) {
                    List<MultipartFile> Hpic = pics.get("RH" + room.getRoomNumber());
                    List<MultipartFile> roomPics = pics.get("R" + room.getRoomNumber());
                    List<String> picUrl = new ArrayList<>();
                    List<String> picUrlPublicId = new ArrayList<>();
                    if (Hpic!=null) {

                        Hpic.forEach(r -> {
                            Map map1 = null;
                            try {
                                map1 = this.cloudinaryImageService.upload(r);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            String url = (String) map1.get("url");
                            String picPublicID = (String) map1.get("public_id");
                            picUrl.add(url);
                            picUrlPublicId.add(picPublicID);
                        });
                        if (!(picUrl.isEmpty() && picUrlPublicId.isEmpty())) {
                            newRoom.setRoomHPic(picUrl.get(0));
                            newRoom.setRoomHPicpublicID(picUrlPublicId.get(0));
                        }
                    }
                    if (roomPics != null) {
                        picUrl.clear();
                        picUrlPublicId.clear();
                        roomPics.forEach(r -> {
                            Map map1 = null;
                            try {
                                map1 = this.cloudinaryImageService.upload(r);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            String url = (String) map1.get("url");
                            String picPublicID = (String) map1.get("public_id");
                            picUrl.add(url);
                            picUrlPublicId.add(picPublicID);
                        });
                        if (!(picUrl.isEmpty() && picUrlPublicId.isEmpty())) {
                            newRoom.setRoomPics(picUrl);
                            newRoom.setRoomPicsPublicID(picUrlPublicId);
                        }
                    }
                }
                newRoom.setHouse(finalHouse);
                roomList.add(newRoom);
            });
        } else {
            System.out.println("No rooms provided.");
        }


        List<Room> newRooms = roomRepository.saveAll(roomList);

        if(invalidRoom.get()>0){
            return "House are added Successfully But some rooms are remove because room number is alread exist ";
        }
        return "Upload successful!";
    }



}
