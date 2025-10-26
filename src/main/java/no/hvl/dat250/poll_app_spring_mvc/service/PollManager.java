package no.hvl.dat250.poll_app_spring_mvc.service;


import no.hvl.dat250.poll_app_spring_mvc.model.Poll;
import no.hvl.dat250.poll_app_spring_mvc.model.User;
import no.hvl.dat250.poll_app_spring_mvc.model.Vote;
import no.hvl.dat250.poll_app_spring_mvc.model.VoteOption;


import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PollManager {

    private final Map<Long, Poll> pollMap = new HashMap<>();
    private final Map<Long, User> userMap = new HashMap<>();

    private final Map<Long, VoteOption> voteOptionMap = new HashMap<>();
    private final Map<Long, Vote> voteMap = new HashMap<>();

    private final AtomicLong nextId = new AtomicLong(1);

    public PollManager() {

    }

    public Collection<Poll> getAllPolls() {
        return pollMap.values();
    }

    public Poll getPollById(long id) {
        return pollMap.get(id);
    }

    public Poll createPoll(Poll poll) {
        long id = nextId.getAndIncrement();
        poll.setId(id);

        if (poll.getVoteOptions() != null) {
            for (VoteOption vo : poll.getVoteOptions()) {
                long voId = nextId.getAndIncrement();
                vo.setId(voId);
                vo.setPoll(poll);
                voteOptionMap.put(voId, vo);
            }
        }

        pollMap.put(id, poll);
        return poll;
    }

    public Poll updatePoll(long id, Poll update) {
        if (!pollMap.containsKey(id)) return null;
        update.setId(id);

        Poll old = pollMap.get(id);
        if (old != null && old.getVoteOptions() != null) {
            for (VoteOption oldVo : old.getVoteOptions()) {
                if (oldVo.getVotes() != null) {
                    for (Vote v : oldVo.getVotes()) {
                        voteMap.remove(v.getId());
                        if (v.getVoter() != null) {
                            v.getVoter().getVotes().removeIf(rv -> rv.getId() == v.getId());
                        }
                    }
                }
                voteOptionMap.remove(oldVo.getId());
            }
        }

        if (update.getVoteOptions() != null) {
            for (VoteOption vo : update.getVoteOptions()) {
                long voId = nextId.getAndIncrement();
                vo.setId(voId);
                vo.setPoll(update);
                voteOptionMap.put(voId, vo);
            }
        }

        pollMap.put(id, update);
        return update;
    }

    public Poll deletePoll(long id) {
        Poll removed = pollMap.remove(id);
        if (removed == null) return null;

        if (removed.getVoteOptions() != null) {
            for (VoteOption vo : removed.getVoteOptions()) {
                if (vo.getVotes() != null) {
                    for (Vote v : vo.getVotes()) {
                        voteMap.remove(v.getId());
                        if (v.getVoter() != null) {
                            v.getVoter().getVotes().removeIf(rv -> rv.getId() == v.getId());
                        }
                    }
                }
                voteOptionMap.remove(vo.getId());
            }
        }

        if (removed.getCreator() != null) {
            removed.getCreator().getCreatedPolls().removeIf(p -> p.getId() == removed.getId());
        }

        return removed;
    }

    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    public User getUserById(long id) {
        return userMap.get(id);
    }

    public User createUser(User user) {
        long id = nextId.getAndIncrement();
        user.setId(id);
        userMap.put(id, user);
        return user;
    }

    public User updateUser(long id, User update) {
        if (!userMap.containsKey(id)) return null;
        update.setId(id);
        userMap.put(id, update);
        return update;
    }

    public User deleteUser(long id) {
        return userMap.remove(id);
    }

    public VoteOption getVoteOptionById(long id) {
        return voteOptionMap.get(id);
    }

    public Vote createVote(long voterId, long voteOptionId, Vote vote) {
        User voter = userMap.get(voterId);
        VoteOption vo = voteOptionMap.get(voteOptionId);
        if (voter == null || vo == null) return null;

        long id = nextId.getAndIncrement();
        vote.setId(id);
        vote.setVoter(voter);
        vote.setVotesOn(vo);
        if (vote.getPublishedAt() == null) {
            vote.setPublishedAt(java.time.Instant.now());
        }

        voteMap.put(id, vote);
        voter.getVotes().add(vote);
        vo.getVotes().add(vote);
        return vote;
    }

    public Collection<Vote> getAllVotes() {
        return voteMap.values();
    }

    public Vote getVoteById(long id) {
        return voteMap.get(id);
    }

    public Vote updateVote(long id, long newVoteOptionId) {
        Vote existing = voteMap.get(id);
        VoteOption newVo = voteOptionMap.get(newVoteOptionId);
        if (existing == null || newVo == null) return null;

        VoteOption oldVo = existing.getVotesOn();
        if (oldVo != null) {
            oldVo.getVotes().removeIf(v -> v.getId() == id);
        }

        existing.setVotesOn(newVo);
        newVo.getVotes().add(existing);
        existing.setPublishedAt(java.time.Instant.now());
        return existing;
    }

    public Vote deleteVote(long id) {
        Vote removed = voteMap.remove(id);
        if (removed == null) return null;

        if (removed.getVoter() != null) {
            removed.getVoter().getVotes().removeIf(v -> v.getId() == id);
        }

        if (removed.getVotesOn() != null) {
            removed.getVotesOn().getVotes().removeIf(v -> v.getId() == id);
        }
        return removed;
    }

    public Poll createPollForUser(long userId, Poll poll) {
        User user = userMap.get(userId);
        if (user == null) return null;
        Poll created = createPoll(poll);
        created.setCreator(user);
        user.getCreatedPolls().add(created);
        return created;
    }
}
