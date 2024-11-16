package room.rental.system.com.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users extends BaseEntity{

    private String email;
    private String password;
    @Enumerated
    private Role role;
    private  String name;
    private  String address;
    private  String contact;
    private  String city;
    private  String state;
    private  String pincode;
}
