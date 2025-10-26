package no.hvl.dat250.poll_app_spring_mvc.controller;

import no.hvl.dat250.poll_app_spring_mvc.model.User;
import no.hvl.dat250.poll_app_spring_mvc.service.PollManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(
    origins = "http://localhost:5173",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowedHeaders = "*",
    allowCredentials = "true",
    maxAge = 3600
)
@RestController
@RequestMapping("/users")
public class UserController {

    private final PollManager pollManager;

    public UserController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return pollManager.createUser(user);
    }

    @GetMapping
    public Collection<User> listUsers() {
        return pollManager.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User u = pollManager.getUserById(id);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User update) {
        User u = pollManager.updateUser(id, update);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        User removed = pollManager.deleteUser(id);
        if (removed == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(removed);
    }
}
