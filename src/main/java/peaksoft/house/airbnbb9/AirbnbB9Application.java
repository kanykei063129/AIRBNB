package peaksoft.house.airbnbb9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class AirbnbB9Application {

    public static void main(String[] args) {
        SpringApplication.run(AirbnbB9Application.class, args);
    }
    @GetMapping
    public String greetings(){
        return "index";
    }

}
