package de.dps.springbatch.writer;

import de.dps.springbatch.model.Person;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Component
public class MyWriter implements ItemWriter<Person> {

    private static final Logger log = LoggerFactory.getLogger(MyWriter.class);

    @Value("${folder.WRITE.OUT}")
    private String outPath;

    @Override
    public void write(List<? extends Person> items) throws Exception {
        for (Person p : items) {
            String csvLine = p.getId() + ";" + p.getFirstName() + ";" + p.getLastName() + "\n";

            DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timeStamp = timeStampPattern.format(LocalDateTime.now());
            String fileName = "outData_" + timeStamp + ".csv";
            FileUtils.writeStringToFile(
                new File(outPath, fileName),
                    csvLine,
                    StandardCharsets.UTF_8,
                    true
            );
        }
    }
}
