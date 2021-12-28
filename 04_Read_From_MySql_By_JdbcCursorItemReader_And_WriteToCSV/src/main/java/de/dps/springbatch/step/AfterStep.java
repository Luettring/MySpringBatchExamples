package de.dps.springbatch.step;

import de.dps.springbatch.config.SqlStatements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author : User
 * @mailto : dp42113@yahoo.de
 * @created : 10.12.2021, Freitag
 * <p>
 * Copyright ® by DPS Software 2021
 **/
@Component
@Profile("DbToCsv")
public class AfterStep implements Tasklet {

    private static final Logger LOGGER= LoggerFactory.getLogger(AfterStep.class);

    private DataSource mySqlDB;

    @Autowired
    public AfterStep(@Qualifier("databaseMySql") DataSource mySqlDB) {
        this.mySqlDB = mySqlDB;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        LOGGER.info("[AfterStep] Drop Table TEMP, dont't need it anymore...");
        try (Connection con = mySqlDB.getConnection()) {
            try (PreparedStatement prepStmt
                         = con.prepareStatement(SqlStatements.MYSQL_DROP_TABLE_TEMP.getRawSqlQuery())) {
                prepStmt.execute();
            }
        }
        return RepeatStatus.FINISHED;
    }
}
