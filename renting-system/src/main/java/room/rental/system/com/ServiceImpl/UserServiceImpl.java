package room.rental.system.com.ServiceImpl;


import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import room.rental.system.com.Dto.UserDto;
import room.rental.system.com.Entity.Role;
import room.rental.system.com.Entity.Users;
import room.rental.system.com.Repository.UserRepository;
import room.rental.system.com.Security.JWTService;
import room.rental.system.com.Security.LoggedInUser;
import room.rental.system.com.Security.UserDeatilsServices;
import room.rental.system.com.Service.UserService;

import java.security.SecureRandom;

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
        userRepository.save(users);
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
//      userDto.setPassword(encoder.encode(newPassword));
//  Users users = databaseService.updateUser(userDto);
////      usersRepo.save(users);
//      mailService.newPasswordMail(users.getEmail() , "New Password " , newPassword , " ... ");
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


}
