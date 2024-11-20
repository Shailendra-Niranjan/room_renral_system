package room.rental.system.com.Service;

import org.apache.coyote.BadRequestException;
import room.rental.system.com.Dto.UserDto;

public interface UserService {

    String registerUser(UserDto userDto );

    String loginUser (UserDto dto) throws BadRequestException;

    String forgetUserPassword(UserDto userDto);


}
