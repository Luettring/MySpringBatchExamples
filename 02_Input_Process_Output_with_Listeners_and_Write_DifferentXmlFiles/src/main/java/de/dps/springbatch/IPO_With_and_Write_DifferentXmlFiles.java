package de.dps.springbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IPO_With_and_Write_DifferentXmlFiles {

    public static void main(String[] args) {
        SpringApplication.run(
            IPO_With_and_Write_DifferentXmlFiles.class, args);
    }

}
