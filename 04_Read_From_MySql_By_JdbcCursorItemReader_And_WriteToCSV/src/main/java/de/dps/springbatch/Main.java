package de.dps.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : User
 * @mailto : dp42113@yahoo.de
 * @created : 09.12.2021, Donnerstag
 * <p>
 * Copyright ® by DPS Software 2021
 **/
@SpringBootApplication
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Start SpringApplication...");
        SpringApplication springApp = new SpringApplication(Main.class);
        springApp.setAdditionalProfiles("DbToCsv");
        springApp.run(args);
    }

}
