package no.hvl.dat250.poll_app_spring_mvc.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "users")
public class User {

    private String username;
    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Set<Poll> created = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Poll> createdPolls = new ArrayList<>();

    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Vote> votes = new ArrayList<>();

    public User() {

    }

//    public User(String username, String email,  long id) {
//        this.username=username;
//        this.email=email;
//        this.id = id;
//    }

    /**
     * Creates a new User object with given username and email.
     * The id of a new user object gets determined by the database.
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.created = new LinkedHashSet<>();
    }

    /**
     * Creates a new Poll object for this user
     * with the given poll question
     * and returns it.
     */
    public Poll createPoll(String question) {
        Instant now = Instant.now();
        Poll newPoll = new Poll(question, now, null);
        newPoll.setCreator(this);
        this.created.add(newPoll);
        this.createdPolls.add(newPoll);
        return newPoll;
    }

    /**
     * Creates a new Vote for a given VoteOption in a Poll
     * and returns the Vote as an object.
     */
    public Vote voteFor(VoteOption option) {
        Instant now = Instant.now();
        Vote vote = new Vote(now, option);
        vote.setVoter(this);
        this.votes.add(vote);
        option.getVotes().add(vote);
        return vote;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Poll> getCreatedPolls() {
        return createdPolls;
    }

    public void setCreatedPolls(List<Poll> createdPolls) {
        this.createdPolls = createdPolls;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
