package de.dps.springbatch;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class SpringBatchApplication_Test {

    @Test
    public void test() {
        String[] args = {};
        Main.main(args);
    }

    @Test
    public void testIf18YearsOld() {
        LocalDate ld = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDate ldBirth = LocalDate.parse("20-03-1962 21:04:05", formatter);
        Long duration = Duration.between(ldBirth.atStartOfDay(),ld.atStartOfDay()).toDays();
        Period period = Period.between(ldBirth, ld);
        System.out.println(duration + " Days / " + period.getYears() + " Years.");
    }

}
