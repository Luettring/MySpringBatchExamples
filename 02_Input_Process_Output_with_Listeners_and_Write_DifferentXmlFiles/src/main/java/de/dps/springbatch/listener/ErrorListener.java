package de.dps.springbatch.listener;

import de.dps.springbatch.model.ErrorItem;
import de.dps.springbatch.model.ErrorType;
import de.dps.springbatch.model.Person;
import de.dps.springbatch.writer.ErrorWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorListener implements SkipListener<Person,Person> {

    private static final Logger log
        = LoggerFactory.getLogger(new Object() {}.getClass().getEnclosingClass());

    public int errCount = 0;
    public boolean process_with_errors = false;

    @Autowired
    private ErrorWriter errorWriter;

    @Override
    public void onSkipInRead(Throwable throwable) {

        String errMsg = throwable.getMessage();

        if (throwable instanceof FlatFileParseException) {
            FlatFileParseException fe = (FlatFileParseException)throwable;

            if (!fe.getInput().contains("id;")) {
                ErrorItem errorItem = new ErrorItem();
                errorItem.setErrorId(++errCount);
                errorItem.setErrorType(ErrorType.FLAT_FILE_ERROR);
                errorItem.setDescription(errMsg);
                errorItem.setOccuredLineNumber(fe.getLineNumber());
                String errLine = fe.getInput();
                errorItem.setErrLine(errLine);
                log.error("onSkipInRead {}", errMsg, errorItem);
                process_with_errors = true;

                markAsError(errLine);
            }
        }
        else {
            throw new RuntimeException("Fehlerhafter Batch: " + errMsg);
        }

    }

    @Override
    public void onSkipInWrite(Person person, Throwable throwable) {
        log.error("onSkipInWrite {}", person, throwable);
    }

    @Override
    public void onSkipInProcess(Person person, Throwable throwable) {
        log.error("onSkipInProcess {}", person, throwable);
    }

    private void markAsError(String errLine) {
        try {
            errorWriter.write(errLine);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
