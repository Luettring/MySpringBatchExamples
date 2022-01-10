package de.dps.springbatch.processor;

import de.dps.springbatch.config.SqlStatements;
import de.dps.springbatch.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

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
        // Is the customer at least 18 years old?
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String birthString = item.getBirthdate().toLocalDate().toString();
        LocalDate ldBirth = LocalDate.parse(birthString, formatter);
        int age = Period.between(ldBirth, LocalDate.now()).getYears();
        if (age >= 18 ) {
            return item;
        }
        LOGGER.info("Customer " + item.getFirstName() + " "  + item.getLastName() +
                " ,Date of birth: " + ldBirth + " is at least not 18 years old...");

        // Delete from Table Customer
        try (Connection con = mySqlDB.getConnection()) {
            try (PreparedStatement prepStmt
                   = con.prepareStatement(SqlStatements.MYSQL_DELETE_TABLE_CUSTOMER
                    .getRawSqlQuery())) {
                prepStmt.setInt(1,item.getId().intValue());
                prepStmt.execute();
            }
        }

        return null;
    }
}