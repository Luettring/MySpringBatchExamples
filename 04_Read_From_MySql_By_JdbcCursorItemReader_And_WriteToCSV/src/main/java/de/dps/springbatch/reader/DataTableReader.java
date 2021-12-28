package de.dps.springbatch.reader;

import de.dps.springbatch.config.SqlStatements;
import de.dps.springbatch.dto.Customer;
import de.dps.springbatch.mapper.CustomerRowMapper;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author : User
 * @mailto : dp42113@yahoo.de
 * @created : 10.12.2021, Freitag
 * <p>
 * Copyright ® by DPS Software 2021
 **/
@Component
@Profile("DbToCsv")
public class DataTableReader extends JdbcCursorItemReader<Customer> {

    @Autowired
    public DataTableReader(@Qualifier("databaseMySql") DataSource mysqlDataSource) throws SQLException {

        setDataSource(mysqlDataSource);
        setSql(SqlStatements.MYSQL_SELECT_CUSTOMER.getRawSqlQuery());
        setRowMapper(new CustomerRowMapper());
    }
}
