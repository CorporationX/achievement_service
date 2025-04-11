package faang.school.achievement.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class AsyncConf {

    @Bean
    public ExecutorService postEventPool() {
        log.info("Initialized thread pool for post event");
        return Executors.newFixedThreadPool(10);
    }
}
