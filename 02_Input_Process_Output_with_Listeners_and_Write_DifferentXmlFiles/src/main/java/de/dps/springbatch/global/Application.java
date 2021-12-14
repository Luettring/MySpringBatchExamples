package de.dps.springbatch.global;

import java.time.Instant;

public class Application {

    // Create a global-wide Instant Object
    public static Instant instant = Instant.parse("1985-12-31T23:59:59.99Z");

}
