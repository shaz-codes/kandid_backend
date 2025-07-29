package me.kandid.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
public class UserBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserBackendApplication.class, args);
    }

}
