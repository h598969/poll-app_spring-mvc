package no.hvl.dat250.poll_app_spring_mvc.controller;

import no.hvl.dat250.poll_app_spring_mvc.model.Poll;
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
public class PollController {

    private final PollManager pollManager;

    public PollController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping("/users/{userId}/polls")
    public ResponseEntity<?> createPollForUser(@PathVariable long userId, @RequestBody Poll poll) {
        Poll created = pollManager.createPollForUser(userId, poll);
        if (created == null) return ResponseEntity.badRequest().body("User not found");
        return ResponseEntity.ok(created);
    }

    @GetMapping("/polls")
    public Collection<Poll> listPolls() {
        return pollManager.getAllPolls();
    }

    @GetMapping("/polls/{id}")
    public ResponseEntity<Poll> getPoll(@PathVariable long id) {
        Poll p = pollManager.getPollById(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PutMapping("/polls/{id}")
    public ResponseEntity<Poll> updatePoll(@PathVariable long id, @RequestBody Poll update) {
        Poll p = pollManager.updatePoll(id, update);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/polls/{id}")
    public ResponseEntity<?> deletePoll(@PathVariable long id) {
        Poll removed = pollManager.deletePoll(id);
        if (removed == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(removed);
    }
}
