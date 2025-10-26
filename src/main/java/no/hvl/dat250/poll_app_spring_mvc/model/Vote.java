package no.hvl.dat250.poll_app_spring_mvc.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant publishedAt;

    @ManyToOne
    @JoinColumn(name = "voter_id")
    private User voter;

    @ManyToOne
    @JoinColumn(name = "vote_option_id")
    private VoteOption votesOn;

    public Vote() {

    }

    public Vote(Instant publishedAt, VoteOption voteOption) {
        this.publishedAt = publishedAt;
        this.votesOn = voteOption;
    }



    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public VoteOption getVotesOn() {
        return votesOn;
    }

    public void setVotesOn(VoteOption votesOn) {
        this.votesOn = votesOn;
    }
}
