package de.dps.springbatch.config;

import de.dps.springbatch.dto.Customer;
import de.dps.springbatch.listener.ErrorListener;
import de.dps.springbatch.processor.FilterProcessor;
import de.dps.springbatch.reader.DataTableReader;
import de.dps.springbatch.step.AfterStep;
import de.dps.springbatch.step.BeforeStep;
import de.dps.springbatch.writer.CsvWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = "de.dps.springbatch")
@Profile("DbToCsv")
public class BatchJobConfig extends DefaultBatchConfigurer {

    @Autowired
    public JobRegistry jobRegistry;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataTableReader dataTableReader;

    // Verarbeitung
    @Autowired
    public FilterProcessor filterProcessor;

    // CSV Ausgabe
    @Autowired
    public CsvWriter csvWriter;

    @Autowired
    public ErrorListener errorListener;

    @Autowired
    public BeforeStep beforeStep;

    @Autowired
    public AfterStep afterStep;

    @Autowired
    @Qualifier("databaseMySql")
    public DataSource dataSource;

    @Value("${config.chunkSize}")
    public Integer chunkSize;

    @Bean
    public Job databaseToCsvFile() {
        return jobBuilderFactory.get("databaseToCsvFile")
                .incrementer(new RunIdIncrementer())
                .start(myBeforeStep())
                .next(readDataToCsv())
                .next(myAfterStep())
                .build();
    }

    @Bean
    public Step myBeforeStep() {
        return stepBuilderFactory.get("myBeforeStep")
                .tasklet(beforeStep)
                .build();
    }

    @Bean
    public Step myAfterStep() {
        return stepBuilderFactory.get("myAfterStep")
                .tasklet(afterStep)
                .build();
    }

    @Bean
    public Step readDataToCsv() {
        return stepBuilderFactory.get("readDataToCsv")
                .<Customer, Customer>chunk(chunkSize)
                .faultTolerant()
                .skipPolicy((t, skipCount) -> true)
                .listener(errorListener)
                .reader(dataTableReader)
                .processor(filterProcessor)
                .writer(csvWriter)
                .build();
    }

//    @Bean
//    public DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost/db_example");
//        dataSource.setUsername("test");
//        dataSource.setPassword("test");
//
//        return dataSource;
//    }

//    @Bean
//    public JdbcCursorItemReader<Customer> cursorItemReader(){
//        JdbcCursorItemReader<Customer> reader = new JdbcCursorItemReader<>();
//        reader.setSql("SELECT id, firstName, lastName, birthdate FROM customer ORDER BY lastName, firstName");
//        reader.setDataSource(dataSource);
//        reader.setFetchSize(100);
//        reader.setRowMapper(new CustomerRowMapper());
//
//        return reader;
//    }


    // This is Thread-safe
//    @Bean
//    public JdbcPagingItemReader<Customer> pagingItemReader(){
//        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
//        reader.setDataSource(this.dataSource);
//        reader.setFetchSize(10);
//        reader.setRowMapper(new CustomerRowMapper());
//
//        Map<String, Order> sortKeys = new HashMap<>();
//        sortKeys.put("id", Order.ASCENDING);
//
//        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
//        queryProvider.setSelectClause("select id, firstName, lastName, birthdate");
//        queryProvider.setFromClause("from customer");
//        queryProvider.setSortKeys(sortKeys);
//
//
//        reader.setQueryProvider(queryProvider);
//
//        return reader;
//    }

//    @Bean
//    public ItemWriter<Customer> customerItemWriter(){
//        return items -> {
//            for(Customer c : items) {
//                System.out.println(c.toString());
//            }
//        };
//    }
//
//    @Bean
//    public Step step1() {
//        return stepBuilderFactory.get("step1")
//                .<Customer, Customer>chunk(chunkSize)
//                .reader(cursorItemReader())
//                .reader(pagingItemReader())
//                .writer(customerItemWriter())
//                .build();
//    }
//
//    @Bean
//    public Job job() {
//        return jobBuilderFactory.get("job")
//                .start(step1())
//                .build();
//    }
}
