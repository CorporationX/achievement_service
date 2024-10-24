package faang.school.achievement.config.handler;

import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class HandlerConfiguration<T> {

    private final List<EventHandler<?>> eventHandlers;

    @Bean
    public Map<Class<?>, List<EventHandler<?>>> eventHandlers() {
        return eventHandlers.stream()
                .collect(Collectors.groupingBy(EventHandler::getInstance,
                        Collectors.toList()));
    }
}
