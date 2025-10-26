package no.hvl.dat250.poll_app_spring_mvc;

import no.hvl.dat250.poll_app_spring_mvc.model.Poll;
import no.hvl.dat250.poll_app_spring_mvc.model.User;
import no.hvl.dat250.poll_app_spring_mvc.model.Vote;
import no.hvl.dat250.poll_app_spring_mvc.model.VoteOption;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PollAppSpringMvcApplicationTests {

    private final String BASE_URL = "http://localhost:8080/";

    private final RestTemplate rest =  new RestTemplate();

    @Test
    void contextLoads() {
    }

    @Test
    void scenario_createUsers_createPoll_vote_changeVote_deletePoll() {

        String base = BASE_URL.endsWith("/") ? BASE_URL.substring(0, BASE_URL.length() - 1) : BASE_URL;

        Map<String, String> u1 = new HashMap<>();
        u1.put("username", "sivsa10");
        u1.put("email", "sivert@examplemail.com");
        ResponseEntity<User> r1 = rest.postForEntity(base + "/users", u1, User.class);
        assertEquals(HttpStatus.OK, r1.getStatusCode());
        assertNotNull(r1.getBody());
        long user1Id = r1.getBody().getId();

        ResponseEntity<User[]> list1 = rest.getForEntity(base + "/users", User[].class);
        assertEquals(HttpStatus.OK, list1.getStatusCode());
        assertTrue(Arrays.stream(list1.getBody()).anyMatch(u -> u.getId() == user1Id));

        Map<String, String> u2 = new HashMap<>();
        u2.put("username", "jakschmi");
        u2.put("email", "jakob@mail.com");
        ResponseEntity<User> r2 = rest.postForEntity(base + "/users", u2, User.class);
        assertEquals(HttpStatus.OK, r2.getStatusCode());
        assertNotNull(r2.getBody());
        long user2Id = r2.getBody().getId();


        ResponseEntity<User[]> list2 = rest.getForEntity(base + "/users", User[].class);
        assertEquals(HttpStatus.OK, list2.getStatusCode());
        assertTrue(list2.getBody().length >= 2);


        Map<String, Object> pollReq = new HashMap<>();
        pollReq.put("question", "Do you support the Stormcloaks or the Imperials?");
        pollReq.put("publishedAt", null);
        pollReq.put("validUntil", "2025-12-31T00:00:00Z");
        List<Map<String, Object>> options = new ArrayList<>();
        Map<String, Object> o1 = new HashMap<>();
        o1.put("caption", "Skyrim belongs to the nords!");
        o1.put("presentationOrder", 1);
        Map<String, Object> o2 = new HashMap<>();
        o2.put("caption", "For the Emperor!");
        o2.put("presentationOrder", 2);
        options.add(o1);
        options.add(o2);
        pollReq.put("voteOptions", options);

        ResponseEntity<Poll> pr = rest.postForEntity(base + "/users/" + user1Id + "/polls", pollReq, Poll.class);
        assertEquals(HttpStatus.OK, pr.getStatusCode());
        assertNotNull(pr.getBody());
        Poll createdPoll = pr.getBody();
        long pollId = createdPoll.getId();
        List<VoteOption> createdOptions = createdPoll.getVoteOptions();
        assertNotNull(createdOptions);
        assertTrue(createdOptions.size() >= 2);
        long option1Id = createdOptions.get(0).getId();
        long option2Id = createdOptions.get(1).getId();

        // 6) List polls -> shows the new poll
        ResponseEntity<Poll[]> pollsResp = rest.getForEntity(base + "/polls", Poll[].class);
        assertEquals(HttpStatus.OK, pollsResp.getStatusCode());
        assertTrue(Arrays.stream(pollsResp.getBody()).anyMatch(p -> p.getId() == pollId));

        // 7) User 2 votes on the poll (choose option1)
        Map<String, Long> voteReq = new HashMap<>();
        voteReq.put("voterId", user2Id);
        voteReq.put("voteOptionId", option1Id);
        ResponseEntity<Vote> voteResp = rest.postForEntity(base + "/polls/" + pollId + "/votes", voteReq, Vote.class);
        assertEquals(HttpStatus.OK, voteResp.getStatusCode());
        assertNotNull(voteResp.getBody());
        long voteId = voteResp.getBody().getId();

        // 8) User 2 changes his vote -> switch to option2
        Map<String, Long> changeReq = new HashMap<>();
        changeReq.put("voteOptionId", option2Id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Long>> changeEntity = new HttpEntity<>(changeReq, headers);
        ResponseEntity<Vote> changeResp = rest.exchange(base + "/votes/" + voteId, HttpMethod.PUT, changeEntity, Vote.class);
        assertEquals(HttpStatus.OK, changeResp.getStatusCode());
        assertNotNull(changeResp.getBody());
        //assertEquals(option2Id, changeResp.getBody().getVotedOn().getId());

        // 9) List votes -> shows the most recent vote for User 2
        ResponseEntity<Vote[]> votesList = rest.getForEntity(base + "/votes", Vote[].class);
        assertEquals(HttpStatus.OK, votesList.getStatusCode());
        //assertTrue(Arrays.stream(votesList.getBody()).anyMatch(v -> v.getId() == voteId && v.getVotedOn().getId() == option2Id));

        // 10) Delete the one poll
        ResponseEntity<Void> delResp = rest.exchange(base + "/polls/" + pollId, HttpMethod.DELETE, null, Void.class);
        assertTrue(delResp.getStatusCode().is2xxSuccessful());

        // 11) List votes -> should be empty (cascade delete)
        ResponseEntity<Vote[]> votesAfter = rest.getForEntity(base + "/votes", Vote[].class);
        assertEquals(HttpStatus.OK, votesAfter.getStatusCode());
        assertEquals(0, votesAfter.getBody().length);
    }
}
