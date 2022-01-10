package de.dps.springbatch.step;

import de.dps.springbatch.config.SqlStatements;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
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
public class BeforeStep implements Tasklet {

    private static final Logger LOGGER= LoggerFactory.getLogger(BeforeStep.class);

    private DataSource mySqlDB;

    @Autowired
    public BeforeStep(@Qualifier("databaseMySql") DataSource mySqlDB,
                      @Value("${folder.WRITE.OUT}") String outPath) {
        this.mySqlDB = mySqlDB;
        try {
            LOGGER.info("[BeforeStep] deleteDirectory: \n" + outPath);
            FileUtils.deleteDirectory(new File(outPath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        LOGGER.info("[BeforeStep] connetc to Database mySqlDB.getConnection");
        try (Connection con = mySqlDB.getConnection()) {
            LOGGER.info("[BeforeStep] Drop Table TEMP");
            try (PreparedStatement prepStmt
                         = con.prepareStatement(SqlStatements.MYSQL_DROP_TABLE_TEMP.getRawSqlQuery())) {
                prepStmt.execute();
            }
            LOGGER.info("[BeforeStep] Create Table TEMP");
            try (PreparedStatement prepStmt
                         = con.prepareStatement(SqlStatements.MYSQL_CREATE_TABLE_TEMP.getRawSqlQuery())) {
                prepStmt.execute();
            }
            LOGGER.info("[BeforeStep] Drop Table CUSTOMER");
            try (PreparedStatement prepStmt
                 = con.prepareStatement(SqlStatements.MYSQL_DROP_TABLE_CUSTOMER.getRawSqlQuery())) {
                prepStmt.execute();
            }
            LOGGER.info("[BeforeStep] Create Table CUSTOMER");
            try (PreparedStatement prepStmt
                         = con.prepareStatement(SqlStatements.MYSQL_CREATE_TABLE_CUSTOMER.getRawSqlQuery())) {
                prepStmt.execute();
            }
            LOGGER.info("[BeforeStep] Insert some records into Table CUSTOMER");
            try (PreparedStatement prepStmt
                         = con.prepareStatement(SqlStatements.MYSQL_INSERT_RECORDS_TABLE_CUSTOMER.getRawSqlQuery())) {
                prepStmt.execute();
            }
        }
        return RepeatStatus.FINISHED;
    }
}
