package de.dps.springbatch.reader;

import de.dps.springbatch.model.Person;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class MyReader {

    @Value("file:${folder.READ.IN}/inputData*.csv")
    private Resource[] inputCSVs;

    @Autowired
    public MyReader() {
    }

    @Bean
    public MultiResourceItemReader<Person> multiResourceItemReader() {
        MultiResourceItemReader<Person> resourceItemReader = new MultiResourceItemReader<>();
        resourceItemReader.setResources(inputCSVs);
        resourceItemReader.setDelegate(reader());

        return resourceItemReader;
    }

    public FlatFileItemReader<Person> reader() {
        FlatFileItemReader<Person> csvReader = new FlatFileItemReader<>();
        csvReader.setLineMapper(createFileLineMapper());

        return csvReader;
    }

    private LineMapper<Person> createFileLineMapper() {
        DefaultLineMapper<Person> fileLineMapper = new DefaultLineMapper<Person>() {

            @Override
            public Person mapLine(String line, int lineNumber) throws Exception {
                Person person = super.mapLine(line, lineNumber);
                person.setFileName(
                    multiResourceItemReader().getCurrentResource().getFilename());
                return super.mapLine(line, lineNumber);
            }
        };

        LineTokenizer tokenizer = createFileLineTokenizer();
        fileLineMapper.setLineTokenizer(tokenizer);

        FieldSetMapper<Person> personFieldSetMapper = createFileInfomationMapper();
        fileLineMapper.setFieldSetMapper(personFieldSetMapper);

        return fileLineMapper;

    }

    private LineTokenizer createFileLineTokenizer() {
        DelimitedLineTokenizer fileLineTokenizer = new DelimitedLineTokenizer();
        fileLineTokenizer.setDelimiter(",");
        fileLineTokenizer.setNames(new String[] { "id", "firstName", "lastName"});

        return fileLineTokenizer;
    }

    private FieldSetMapper<Person> createFileInfomationMapper() {
        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);

        return fieldSetMapper;
    }

}
