package no.hvl.dat250.poll_app_spring_mvc.controller;

import no.hvl.dat250.poll_app_spring_mvc.model.Vote;
import no.hvl.dat250.poll_app_spring_mvc.model.VoteOption;
import no.hvl.dat250.poll_app_spring_mvc.service.PollManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@CrossOrigin(
    origins = "http://localhost:5173",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowedHeaders = "*",
    allowCredentials = "true",
    maxAge = 3600
)
@RestController
public class VoteController {

    private final PollManager pollManager;

    public VoteController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping("/polls/{pollId}/votes")
    public ResponseEntity<?> createVote(@PathVariable long pollId, @RequestBody Map<String, Long> body) {
        Long voterId = body.get("voterId");
        Long voteOptionId = body.get("voteOptionId");
        if (voterId == null || voteOptionId == null) {
            return ResponseEntity.badRequest().body("voterId and voteOptionId required");
        }
        VoteOption vo = pollManager.getVoteOptionById(voteOptionId);
        if (vo == null || vo.getPoll() == null || vo.getPoll().getId() != pollId) {
            return ResponseEntity.badRequest().body("voteOption not found for poll");
        }
        Vote vote = new Vote();
        Vote created = pollManager.createVote(voterId, voteOptionId, vote);
        if (created == null) return ResponseEntity.badRequest().body("voter or voteOption not found");
        return ResponseEntity.ok(created);
    }

    @PutMapping("/votes/{voteId}")
    public ResponseEntity<?> updateVote(@PathVariable long voteId, @RequestBody Map<String, Long> body) {
        Long newVoId = body.get("voteOptionId");
        if (newVoId == null) return ResponseEntity.badRequest().body("voteOptionId required");
        Vote updated = pollManager.updateVote(voteId, newVoId);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/votes")
    public Collection<Vote> listVotes() {
        return pollManager.getAllVotes();
    }

    @GetMapping("/votes/{id}")
    public ResponseEntity<Vote> getVote(@PathVariable long id) {
        Vote v = pollManager.getVoteById(id);
        if (v == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(v);
    }

    @DeleteMapping("/votes/{id}")
    public ResponseEntity<?> deleteVote(@PathVariable long id) {
        Vote removed = pollManager.deleteVote(id);
        if (removed == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(removed);
    }
}
