package de.dps.springbatch.mapper;

import de.dps.springbatch.dto.Customer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@ComponentScan(basePackages = "de.dps.springbatch")
@Profile("DbToCsv")
public class CustomerRowMapper implements RowMapper<Customer> {
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        //@// @formatter:off
        return Customer.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("firstName"))
                .lastName(rs.getString("lastName"))
                .birthdate(LocalDateTime.parse(rs.getString("birthdate"), DT_FORMAT))
                .build();
        // @formatter:on
    }
}