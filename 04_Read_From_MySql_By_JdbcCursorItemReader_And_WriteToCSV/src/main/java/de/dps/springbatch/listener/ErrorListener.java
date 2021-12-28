package de.dps.springbatch.listener;

import de.dps.springbatch.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author : User
 * @mailto : dp42113@yahoo.de
 * @created : 10.12.2021, Freitag
 * <p>
 * Copyright ® by DPS Software 2021
 **/
@Component
@ComponentScan(basePackages = "de.dps.springbatch")
@Profile("DbToCsv")
public class ErrorListener implements SkipListener<Customer, Customer> {

    private static final Logger LOGGER= LoggerFactory.getLogger(ErrorListener.class);

    @Override
    public void onSkipInRead(Throwable throwable) {
        LOGGER.error("onReadError", throwable);
    }

    @Override
    public void onSkipInWrite(Customer item, Throwable throwable) {
        if (throwable instanceof Exception) {
            LOGGER.error(item.toString(), throwable.getMessage());
            return;
        }
        LOGGER.info(item.toString());
    }

    @Override
    public void onSkipInProcess(Customer item, Throwable throwable) {
        LOGGER.info(item.toString());
    }
}
