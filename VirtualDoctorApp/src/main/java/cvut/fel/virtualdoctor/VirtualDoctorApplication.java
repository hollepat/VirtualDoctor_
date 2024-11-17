package cvut.fel.virtualdoctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the application.
 * To set mongoDB connection: https://www.youtube.com/watch?v=ssj0CGxv60k
 */

@SpringBootApplication
public class VirtualDoctorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualDoctorApplication.class, args);
    }

}
