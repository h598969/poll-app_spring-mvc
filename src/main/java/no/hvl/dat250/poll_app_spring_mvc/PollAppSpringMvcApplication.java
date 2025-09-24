package no.hvl.dat250.poll_app_spring_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PollAppSpringMvcApplication {

	public static void main(String[] args) {

        SpringApplication.run(PollAppSpringMvcApplication.class, args);
	}

    @GetMapping("/greetings")
    public String hello (@RequestParam(value="name", defaultValue="World") String name) {
        return String.format("Hello %s!", name);
    }

}
