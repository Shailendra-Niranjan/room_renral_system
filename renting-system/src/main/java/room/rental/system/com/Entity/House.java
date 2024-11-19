package room.rental.system.com.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class House extends BaseEntity{
    private String houseName;
    private  String address;
    private  String contact;
    private  String city;
    private  String state;
    private  String pincode;
    private String description;
    private long bookedRoom;
    private String houseHPic;
    private String houseHPicPublicID;
    private List<String> pics;
    private List<String> picsPublicID;

    @ManyToOne
    @JoinColumn(name = "owner_id") // optional, specifies the foreign key column
    @JsonBackReference
    private Users owner;

    @OneToMany(mappedBy = "house") // Matches the field name in House entity
    @JsonManagedReference
    private List<Room> rooms;

}
