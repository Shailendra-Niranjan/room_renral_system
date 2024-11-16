package room.rental.system.com.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import room.rental.system.com.Entity.Todo;
import room.rental.system.com.Repository.TodoRepo;

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

}
