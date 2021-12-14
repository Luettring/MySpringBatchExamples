package de.dps.springbatch.config;

import de.dps.springbatch.listener.AfterStep;
import de.dps.springbatch.listener.BeforeStep;
import de.dps.springbatch.listener.ErrorListener;
import de.dps.springbatch.model.Gender;
import de.dps.springbatch.model.Person;
import de.dps.springbatch.processor.MyProcessor;
import de.dps.springbatch.tasklet.FileDeletingTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableBatchProcessing
public class IPO_BatchConfig extends DefaultBatchConfigurer {

    @Autowired
    public JobRegistry jobRegistry;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /*
       IPO-Prinzip - Input ... Process ... Output
     */

    // Input
    @Autowired
    private MultiResourceItemReader<Person> myReader;

    // Process
    @Autowired
    private MyProcessor myProcessor;

    // Errorhandling
    @Autowired
    private ErrorListener errorListener;

    // Before Step Execution
    @Autowired
    private BeforeStep beforeStep;

    // After Step Execution
    @Autowired
    private AfterStep afterStep;

    @Value("${config.chunkSize}")
    private Integer chunkSize;

    @Value("${folder.WRITE.OUT}")
    private String outPath;

    @Bean
    public Job readCSVFilesJob() {
        return jobBuilderFactory
                .get("ipo_batch_processing")
                .incrementer(new RunIdIncrementer())
                .start(deleteFilesInDir())
                .next(myBeforeStep())
                .next(stepReadCSVwriteXML())
                .next(myAfterStep())
                .build();
    }

    @Bean
    public Step deleteFilesInDir() {
        return this.stepBuilderFactory.get("deleteFilesInDir")
                .tasklet(fileDeletingTasklet())
                .build();
    }

    @Bean
    public FileDeletingTasklet fileDeletingTasklet() {
        FileDeletingTasklet tasklet = new FileDeletingTasklet();

        tasklet.setDirectoryResource(new FileSystemResource("src/main/resources/error"));

        return tasklet;
    }

    @Bean
    public Step myBeforeStep() {
        return stepBuilderFactory.get("myBeforeStep")
            .tasklet(beforeStep)
            .build();
    }

    @Bean
    public Step stepReadCSVwriteXML() {
        return stepBuilderFactory.get("stepReadCSVwriteXML")
                .<Person, Person>chunk(chunkSize)
                .faultTolerant()
                .skipPolicy((t, skipcount) -> true)
                .listener(errorListener)
                .reader(myReader)
                .processor(myProcessor)
                .writer(classifierCompositeItemWriter(femaleItemWriter(), maleItemWriter()))
                .stream(femaleItemWriter())
                .stream(maleItemWriter())
                .build();
    }

    @Bean
    public Step myAfterStep() {
        return stepBuilderFactory.get("myAfterStep")
                .tasklet(afterStep)
                .build();
    }


    @Bean
    public ClassifierCompositeItemWriter<Person> classifierCompositeItemWriter(
            ItemWriter<Person> femaleItemWriter,
            ItemWriter<Person> maleItemWriter
    ) {
        ClassifierCompositeItemWriter<Person> classifierCompositeItemWriter = new ClassifierCompositeItemWriter<>();
        classifierCompositeItemWriter.setClassifier(new Classifier<Person, ItemWriter<? super Person>>() {
            @Override
            public ItemWriter<? super Person> classify(Person person) {
                if (person.getGender().equals(Gender.F)) {
                    return femaleItemWriter;
                } else {
                    return maleItemWriter;
                }
            }
        });
        return classifierCompositeItemWriter;
    }

    /*
        https://stackoverflow.com/questions/23089159/why-does-destroy-method-close-fail-for-jpapagingitemreader-configured-with-jav
     */
    @Bean(destroyMethod="")
    public StaxEventItemWriter<Person> femaleItemWriter() {
        StaxEventItemWriter<Person> itemWriter = getPersonStaxEventItemWriter();
        itemWriter.setResource(new FileSystemResource(outPath + "female.xml"));

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Person.class);
        itemWriter.setMarshaller(marshaller);

        return itemWriter;
    }

    @Bean(destroyMethod="")
    public StaxEventItemWriter<Person> maleItemWriter() {
        StaxEventItemWriter<Person> itemWriter = getPersonStaxEventItemWriter();
        itemWriter.setResource(new FileSystemResource(outPath + "male.xml"));

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Person.class);
        itemWriter.setMarshaller(marshaller);

        return itemWriter;
    }

    private StaxEventItemWriter<Person> getPersonStaxEventItemWriter() {
        StaxEventItemWriter<Person> itemWriter = new StaxEventItemWriter<>();
        itemWriter.setRootTagName("persons");

        return itemWriter;
    }
}