package de.dps.springbatch.config;

import de.dps.springbatch.model.Person;
import de.dps.springbatch.processor.FilterProcessor;
import de.dps.springbatch.writer.MyWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

    @Autowired
    public JobRegistry jobRegistry;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /*
       EVA-Prinzip - Eingabe ... Verarbeitung ... Ausgabe
     */

    // Eingabe
    @Autowired
    private MultiResourceItemReader<Person> reader;

    // Verarbeitung
    @Autowired
    private FilterProcessor filterProcessor;

    // Ausgabe
    @Autowired
    private MyWriter writer;

    @Bean
    public Job readCSVFilesJob() {
        return jobBuilderFactory
                .get("readCSVFilesJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<Person, Person>chunk(5)
                .reader(reader)
                .processor(filterProcessor)
                .writer(writer)
                .build();
    }

}