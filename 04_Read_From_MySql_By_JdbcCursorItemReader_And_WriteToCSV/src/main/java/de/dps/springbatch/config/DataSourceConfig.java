package de.dps.springbatch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author : User
 * @mailto : dp42113@yahoo.de
 * @created : 10.12.2021, Freitag
 * <p>
 * Copyright ® by DPS Software 2021
 **/
@Component
@ComponentScan(basePackages = "de.dps.springbatch")
@Profile({"DbToCsv", "DbLoad"})
public class DataSourceConfig {

    @Configuration
    @ConfigurationProperties("database.spring")
    public class DatabaseSpringConfig extends HikariConfig {

        @Bean
        @Primary
        public DataSource springDataSource() {
            return new HikariDataSource(this);
        }
    }

    @Configuration
    @ConfigurationProperties("mysql.datasource")
    public class DatabaseMysqlConfig extends HikariConfig {

        @Bean(name = "databaseMySql")
        public DataSource mysqlDataSource() {
            return new HikariDataSource(this);
        }
    }
}
