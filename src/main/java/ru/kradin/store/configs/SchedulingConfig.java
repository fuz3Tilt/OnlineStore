package ru.kradin.store.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kradin.store.utils.MyScheduling;

@Configuration
public class SchedulingConfig {

    @Bean
    public MyScheduling myScheduling() {
        MyScheduling myScheduling = new MyScheduling();
        myScheduling.createTasks();
        return myScheduling;
    }
}
