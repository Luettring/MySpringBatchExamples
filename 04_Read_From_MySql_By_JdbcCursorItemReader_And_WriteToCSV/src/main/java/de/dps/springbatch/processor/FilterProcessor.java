package de.dps.springbatch.processor;

import de.dps.springbatch.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Profile("DbToCsv")
public class FilterProcessor implements ItemProcessor<Customer, Customer> {

    private static final Logger LOGGER= LoggerFactory.getLogger(FilterProcessor.class);

    private DataSource mySqlDB;

    boolean header = false;

    @Autowired
    public FilterProcessor(@Qualifier("databaseMySql") DataSource mySqlDB) {
        this.mySqlDB = mySqlDB;
    }

    @Override
    public Customer process(Customer item) throws Exception {

        LOGGER.info("processing...");
        if (item.toString().contains("Customer{id='id'")) {
            if (header) {
                return null;
            }
            header = true;
        }
        return item;
    }
}