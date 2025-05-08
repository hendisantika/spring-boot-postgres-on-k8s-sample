package id.my.hendisantika.postgresonk8ssample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class SpringBootPostgresOnK8sSampleApplicationTests {

    @Test
    void contextLoads() {
        try (PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17.4")) {
            postgresContainer.start();

            System.out.println("PostgreSQL URL: " + postgresContainer.getJdbcUrl());
            System.out.println("Username: " + postgresContainer.getUsername());
            System.out.println("Password: " + postgresContainer.getPassword());
        }
    }

}
