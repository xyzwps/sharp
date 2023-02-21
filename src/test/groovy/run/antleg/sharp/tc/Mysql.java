package run.antleg.sharp.tc;

import org.testcontainers.containers.MySQLContainer;

public class Mysql extends MySQLContainer<Mysql> {
    private static final String IMAGE_VERSION = "mysql:5.7.41";

    private Mysql(String version) {
        super(version);
    }

    private static final class MysqlHolder {
        private static final Mysql mysql = new Mysql(IMAGE_VERSION)
                .withDatabaseName("sharp");
        static {
            mysql.start();
        }
    }

    public static Mysql getInstance() {
        return MysqlHolder.mysql;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TC_MYSQL_URL", MysqlHolder.mysql.getJdbcUrl());
        System.setProperty("TC_MYSQL_USERNAME", MysqlHolder.mysql.getUsername());
        System.setProperty("TC_MYSQL_PASSWORD", MysqlHolder.mysql.getPassword());
    }

    @Override
    public void stop() {
        // see https://www.baeldung.com/spring-boot-testcontainers-integration-test
    }
}
