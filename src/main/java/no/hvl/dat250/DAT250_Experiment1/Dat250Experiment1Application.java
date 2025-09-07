package no.hvl.dat250.DAT250_Experiment1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Dat250Experiment1Application {

	public static void main(String[] args) {

        SpringApplication.run(Dat250Experiment1Application.class, args);
	}

    @GetMapping("/greetings")
    public String hello (@RequestParam(value="name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

}
