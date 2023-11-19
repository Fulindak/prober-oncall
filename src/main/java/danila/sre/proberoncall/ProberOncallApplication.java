package danila.sre.proberoncall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@EnableConfigurationProperties
@SpringBootApplication
public class ProberOncallApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProberOncallApplication.class, args);
    }

}
