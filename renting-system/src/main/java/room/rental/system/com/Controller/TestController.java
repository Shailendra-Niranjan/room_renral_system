package room.rental.system.com.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import room.rental.system.com.Entity.Todo;
import room.rental.system.com.Repository.TodoRepo;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    TodoRepo todoRepo;

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


    @PostMapping("/upload/house")
    public String uploadFiles(
            @RequestParam String house,
            @RequestParam(required = false) List<String> rooms,
            @RequestParam(required = false) MultiValueMap<String, MultipartFile> pics
    ) {
        // Process the house
        System.out.println("House: " + house);

        // Process the rooms
        if (rooms != null) {
            System.out.println("Rooms: " + String.join(", ", rooms));
        } else {
            System.out.println("No rooms provided.");
        }

        // Process the pictures
        if (pics != null && !pics.isEmpty()) {
            pics.forEach((key, files) -> {
                System.out.println("Key: " + key);
                files.forEach(file -> {
                    System.out.println("File Name: " + file.getOriginalFilename());
                    System.out.println("File Size: " + file.getSize());
                });
            });
        } else {
            System.out.println("No pictures uploaded.");
        }

        return "Upload successful!";
    }
}
