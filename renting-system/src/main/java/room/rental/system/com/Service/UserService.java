package room.rental.system.com.Service;

import org.apache.coyote.BadRequestException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import room.rental.system.com.Dto.UserDto;

import java.util.List;

public interface UserService {

    String registerUser(UserDto userDto );

    String loginUser (UserDto dto) throws BadRequestException;

    String forgetUserPassword(UserDto userDto);
    String createHouose(String house, List<String> rooms, MultiValueMap<String, MultipartFile> pics);

}
