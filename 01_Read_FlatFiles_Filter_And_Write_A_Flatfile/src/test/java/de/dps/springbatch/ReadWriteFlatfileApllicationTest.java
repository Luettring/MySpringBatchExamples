package de.dps.springbatch;

import de.dps.springbatch.config.BatchConfig;
import de.dps.springbatch.processor.FilterProcessor;
import de.dps.springbatch.reader.MyReader;
import de.dps.springbatch.writer.MyWriter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;

@RunWith( SpringRunner.class )
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                BatchConfig.class,
                MyReader.class,
                FilterProcessor.class,
                MyWriter.class,
                JobLauncherTestUtils.class
        }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ReadWriteFlatfileApllicationTest
{
    private static Logger log = LoggerFactory.getLogger(ReadWriteFlatfileApllicationTest.class);

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    private SimpleDateFormat df = new SimpleDateFormat( "HH:mm:ss.SSS" );

    @Test
    public void testReadWriteFlatFiles() throws Exception
    {
        log.info("\nTest ReadWriteFlatFiles:" );
        JobExecution je = jobLauncher.launchJob();

        // Ueberpruefe Job-Ergebnis:
        Assert.assertEquals( BatchStatus.COMPLETED, je.getStatus() );
        Assert.assertEquals( ExitStatus.COMPLETED,  je.getExitStatus() );
        Assert.assertEquals( 0, je.getAllFailureExceptions().size() );
        Assert.assertEquals( 1, je.getStepExecutions().size() );
        double dauerSum = 0.;
        for( StepExecution se : je.getStepExecutions() ) {
            Assert.assertEquals( BatchStatus.COMPLETED, se.getStatus() );
            Assert.assertEquals( ExitStatus.COMPLETED,  se.getExitStatus() );
            Assert.assertEquals( 4, se.getCommitCount() );
            double dauer = ((double) ((se.getEndTime().getTime() - se.getStartTime().getTime()) / 100)) / 10;
            dauerSum += dauer;
            log.info( se.getStepName() + ": von " + df.format( se.getStartTime() ) +
                    " bis " + df.format( se.getEndTime() ) +
                    " h, Dauer: " + dauer + " s" );
        }
        log.info( "Summe der Steps: " + dauerSum + " s" );
        double dauerJob = ((double) ((je.getEndTime().getTime() - je.getStartTime().getTime()) / 100)) / 10;
        log.info( "Gesamtdauer Job: " + dauerJob + " s" );
    }
}
