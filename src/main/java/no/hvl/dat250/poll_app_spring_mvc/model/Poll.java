package no.hvl.dat250.poll_app_spring_mvc.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VoteOption> options = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User createdBy;

    public Poll() {

    }

    public Poll(String question, Instant publishedAt, Instant validUntil) {
        this.question = question;
        this.publishedAt = publishedAt;
        this.validUntil = validUntil;
    }

    public Poll(String question, Instant publishedAt, Instant validUntil, Long id) {
        this.question = question;
        this.publishedAt = publishedAt;
        this.validUntil = validUntil;
        this.id = id;
    }

    /**
     *
     * Adds a new option to this Poll and returns the respective
     * VoteOption object with the given caption.
     */
    public VoteOption addVoteOption(String caption) {
        int presOrder = this.options.size();
        VoteOption voteOption = new VoteOption(caption, presOrder);
        voteOption.setPoll(this);
        this.options.add(voteOption);
        return voteOption;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<VoteOption> getVoteOptions() {
        return options;
    }

    public void setVoteOptions(List<VoteOption> voteOptions) {
        this.options = voteOptions;
    }

    public User getCreator() {
        return createdBy;
    }

    public void setCreator(User creator) {
        this.createdBy = creator;
    }

}
