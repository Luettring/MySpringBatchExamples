package de.dps.springbatch.processor;

import de.dps.springbatch.model.Person;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FilterProcessor implements ItemProcessor<Person, Person> {

    public FilterProcessor() {
    }

    @Override
    public Person process(Person item) throws Exception {
        return item;
    }
}
