package de.dps.springbatch.config;

/**
 * @author : User
 * @mailto : dp42113@yahoo.de
 * @created : 10.12.2021, Freitag
 * <p>
 * Copyright ® by DPS Software 2021
 **/
public enum SqlStatements {

    /*
        MySQL Database
     */
    MYSQL_SELECT_CUSTOMER("SELECT id, firstName, lastName, " +
                                 "birthdate FROM db_example.customer"),

    MYSQL_DROP_TABLE_CUSTOMER("DROP TABLE IF EXISTS db_example.customer"),

    MYSQL_CREATE_TABLE_CUSTOMER("CREATE TABLE db_example.customer (\n" +
            "  `id` MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
            "  `firstName` VARCHAR(255) NULL,\n" +
            "  `lastName` VARCHAR(255) NULL,\n" +
            "  `birthdate` VARCHAR(255) NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") AUTO_INCREMENT=1;"),

    MYSQL_INSERT_RECORDS_TABLE_CUSTOMER(
            "INSERT INTO db_example.customer (`id`, `firstName`, `lastName`, `birthdate`) " + '\n' +
            "VALUES ('1', 'John', 'Doe', '10-10-1952 10:22:10')," + '\n' +
            "       ('2', 'Amy', 'Eugene', '05-07-1985 17:10:00')," + '\n' +
            "       ('3', 'Laverne', 'Mann', '11-12-1988 18:10:00')," + '\n' +
            "       ('4', 'Janice', 'Preston', '19-02-1960 19:12:00')," + '\n' +
            "       ('5', 'Pauline', 'Rios', '29-08-2007 22:12:10')," + '\n' +
            "       ('6', 'Perry', 'Burnside', '10-03-1981 11:11:02')," + '\n' +
            "       ('7', 'Todd', 'Kinsey', '14-12-1998 23:14:20')," + '\n' +
            "       ('8', 'Jacqueline', 'Hyde', '20-03-1983 16:22:05')," + '\n' +
            "       ('9', 'Rico', 'Hale', '10-10-2000 12:10:23')," + '\n' +
            "       ('10', 'Samuel', 'Lamm', '11-11-1999 14:17:03')," + '\n' +
            "       ('11', 'Robert', 'Coster', '10-10-1972 18:11:04')," + '\n' +
            "       ('12', 'Tamara', 'Soler', '02-01-1978 07:10:10')," + '\n' +
            "       ('13', 'Justin', 'Kramer', '19-11-1951 11:10:05')," + '\n' +
            "       ('14', 'Andrea', 'Law', '14-10-1959 10:14:10')," + '\n' +
            "       ('15', 'Laura', 'Porter', '12-12-2010 11:13:06')," + '\n' +
            "       ('16', 'Michael', 'Cantu', '11-04-1999 12:10:10')," + '\n' +
            "       ('17', 'Andrew', 'Thomas', '04-05-1967 21:22:07')," + '\n' +
            "       ('18', 'Jose', 'Hannah', '16-09-1950 05:13:10')," + '\n' +
            "       ('19', 'Valerie', 'Hilbert', '13-06-1966 23:18:10')," + '\n' +
            "       ('20', 'Patrick', 'Durham', '12-10-1978 10:10:10')," + '\n' +
            "       ('21', 'Günter', 'Hüßloch', '20-03-1962 21:04:00')"),

    MYSQL_DELETE_TABLE_CUSTOMER("DELETE FROM CUSTOMER WHERE ID = ?"),

    MYSQL_DROP_TABLE_TEMP("DROP TABLE IF EXISTS db_example.temp"),

    MYSQL_CREATE_TABLE_TEMP("CREATE TABLE db_example.temp (\n" +
            "  `id` MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
            "  `tempField1` VARCHAR(255) NULL,\n" +
            "  `tempField2` VARCHAR(255) NULL,\n" +
            "  `tempField3` VARCHAR(255) NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") AUTO_INCREMENT=1;");

    private final String rawSqlQuery;
    SqlStatements(final String sqlQuery) {
        this.rawSqlQuery = sqlQuery;
    }

    public String getRawSqlQuery() {
        return rawSqlQuery;
    }

    public static SqlStatements getEnum(String value) {
        return value != null ? SqlStatements.valueOf(value) : null;
    }
}
