package de.dps.springbatch.processor;

import de.dps.springbatch.global.Application;
import de.dps.springbatch.model.Person;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class MyProcessor implements ItemProcessor<Person, Person> {

    public MyProcessor() {
    }

    @Override
    public Person process(Person item) throws Exception {

        if (item == null) {
            return null;
        }

        // If item contains header then nor processing
        if (String.valueOf(item.getId()).toUpperCase().equals("ID")) {
            return null;
        }

        // Filter any records wih equal/less given Date?
        LocalDate birthday = item.getBirthday();
        Instant persInstant = birthday.atStartOfDay(ZoneId.systemDefault()).toInstant();
        if (persInstant.isAfter(Application.instant)) {
            return item;
        }
        return null;
    }
}
