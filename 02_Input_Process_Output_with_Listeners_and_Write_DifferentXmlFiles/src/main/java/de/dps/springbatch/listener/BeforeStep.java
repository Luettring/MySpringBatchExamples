package de.dps.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class BeforeStep implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(BeforeStep.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("[BeforeStep]: " +
                chunkContext.getStepContext().getJobName() + " / " +
                chunkContext.getStepContext().getStepName());

        return RepeatStatus.FINISHED;
    }
}
