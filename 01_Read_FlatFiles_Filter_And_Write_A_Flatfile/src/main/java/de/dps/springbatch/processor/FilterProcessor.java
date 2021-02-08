package de.dps.springbatch.processor;

import de.dps.springbatch.model.Person;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FilterProcessor implements ItemProcessor<Person, Person> {

    boolean header = false;

    public FilterProcessor() {
    }

    @Override
    public Person process(Person item) throws Exception {

        if (item.toString().contains("Person{id='id'")) {
            if (header) {
                return null;
            }
            header = true;
        }
        return item;
    }
}
