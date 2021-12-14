package de.dps.springbatch;

import de.dps.springbatch.adapter.GenderXmlAdapter;
import de.dps.springbatch.adapter.IntegerXmlAdapter;
import de.dps.springbatch.adapter.LocalDateXmlAdapter;
import de.dps.springbatch.config.IPO_BatchConfig;
import de.dps.springbatch.listener.AfterStep;
import de.dps.springbatch.listener.BeforeStep;
import de.dps.springbatch.listener.ErrorListener;
import de.dps.springbatch.processor.MyProcessor;
import de.dps.springbatch.reader.MyReader;
import de.dps.springbatch.writer.ErrorWriter;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.text.SimpleDateFormat;

//@RunWith( SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                IPO_BatchConfig.class,
                ErrorListener.class,
                ErrorWriter.class,
                BeforeStep.class,
                AfterStep.class,
                GenderXmlAdapter.class,
                IntegerXmlAdapter.class,
                LocalDateXmlAdapter.class,
                MyReader.class,
                MyProcessor.class,
                JobLauncherTestUtils.class
        }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class IPO_With_Listeners_and_Scheduling_Test
{
    private static Logger log = LoggerFactory.getLogger(IPO_With_Listeners_and_Scheduling_Test.class);

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    private SimpleDateFormat df = new SimpleDateFormat( "HH:mm:ss.SSS" );

    @Value("#{new Integer('${config.chunkSize}')}")
    private Integer chunkSize;

    @Test
    public void test_StepReadCSVwriteXML() throws Exception
    {
        log.info("\nTest StepReadCSVwriteXML:" );
        JobExecution je = jobLauncher.launchJob();

        // Ueberpruefe Job-Ergebnis:
        Assert.assertEquals( BatchStatus.COMPLETED, je.getStatus() );
        Assert.assertEquals( ExitStatus.COMPLETED,  je.getExitStatus() );
        Assert.assertEquals( 0, je.getAllFailureExceptions().size() );
        Assert.assertEquals( 4, je.getStepExecutions().size() );
        double dauerSum = 0.;
        for( StepExecution se : je.getStepExecutions() ) {
            Assert.assertEquals( BatchStatus.COMPLETED, se.getStatus() );
            Assert.assertEquals( ExitStatus.COMPLETED,  se.getExitStatus() );
            if (se.getStepName().equals("stepReadCSVwriteXML")) {
                Assert.assertEquals( 6, se.getCommitCount() );
                Assert.assertEquals(25,se.getReadCount());
                Assert.assertEquals(17,se.getWriteCount());
                Assert.assertEquals(7,se.getSkipCount());
            }

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
