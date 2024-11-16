package room.rental.system.com.Controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import room.rental.system.com.Dto.UserDto;
import room.rental.system.com.Service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register (@RequestBody UserDto userDto) {
         return ResponseEntity.ok(Map.of("Message" ,userService.registerUser(userDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) throws BadRequestException {

        return ResponseEntity.ok(userService.loginUser(userDto));
    }
    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.forgetUserPassword(userDto));
    }

}
