package run.antleg.sharp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SharpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SharpApplication.class, args);
	}

}