package room.rental.system.com.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Room extends BaseEntity{
    private String roomNumber;
    private String rentPrice;
    private boolean availabilityStatus;
    private String houseName;
    private String description;
    private String roomHPic;
    private String roomHPicpublicID;
    private List<String> roomPics;
    private List<String> roomPicsPublicID;

    @ManyToOne
    @JoinColumn(name = "house_id")
    @JsonBackReference
    private House house;



}
