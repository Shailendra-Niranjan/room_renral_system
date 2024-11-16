package room.rental.system.com.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import room.rental.system.com.Entity.Users;
import room.rental.system.com.Repository.UserRepository;

@Service
public class UserDeatilsServices implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepository.findUsersByEmail(username);

        if(user == null){
            System.out.println(" user not found ");
            throw  new UsernameNotFoundException("User not found "+ username );
        }
        System.out.println(user.toString());
        return new LoggedInUser(user);
    }
}
