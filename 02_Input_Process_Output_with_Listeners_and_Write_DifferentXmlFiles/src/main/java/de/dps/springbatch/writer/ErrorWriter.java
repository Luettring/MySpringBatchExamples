package de.dps.springbatch.writer;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ErrorWriter {

    private static final Logger log = LoggerFactory.getLogger(ErrorWriter.class);
    private boolean Header = false;

    @Value("${folder.WRITE.ERROR}")
    private String errorPath;

    public void write(String errLine) throws Exception {
        DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStamp = timeStampPattern.format(LocalDateTime.now());
        String fileName = "errorData_" + timeStamp + ".csv";

        if (!Header) {
            Header = true;

            FileUtils.writeStringToFile(
                    new File(errorPath, fileName),
                    "id;firstName;lastName;gender;birthday" + '\n',
                    StandardCharsets.UTF_8,
                    true);
        }
        FileUtils.writeStringToFile(
                new File(errorPath, fileName),
                errLine + '\n',
                StandardCharsets.UTF_8,
                true);
    }
}
