package de.dps.springbatch.writer;

import de.dps.springbatch.dto.Customer;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author : User
 * @mailto : dp42113@yahoo.de
 * @created : 10.12.2021, Freitag
 * <p>
 * Copyright ® by DPS Software 2021
 **/
@Component
@Profile("DbToCsv")
public class CsvWriter implements ItemWriter<Customer> {

    private static final Logger LOGGER= LoggerFactory.getLogger(CsvWriter.class);
    private boolean writeHeader = false;

    @Value("${folder.WRITE.OUT}")
    private String outPath;

    @Override
    public void write(List<? extends Customer> items) throws Exception {
        for (Customer p : items) {
            if (!writeHeader && p.getId().equals("id")) {
                writeHeader = true;
            }
            String csvLine = p.getId() + ";" + p.getFirstName() + ";" +
                    p.getLastName() + ";" + formatBirthDay(p.getBirthdate()) + "\n";

            DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMdd");
            String timeStamp = timeStampPattern.format(LocalDateTime.now());
            String fileName = "outData_" + timeStamp + ".csv";
            LOGGER.info("Append item to File '" + fileName + "' ...");
            FileUtils.writeStringToFile(
                    new File(outPath, fileName),
                    csvLine,
                    StandardCharsets.UTF_8,
                    true
            );
        }
    }

    private String formatBirthDay(LocalDateTime birthday) {

        // using short german date/time formatting (01.04.2014 21:45)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH.mm");
        return birthday.format(formatter);
    }
}

