package faang.school.achievement.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    private final String serviceName = "Service";

    @BeforeEach
    void setUp() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ReflectionTestUtils.setField(globalExceptionHandler, "serviceName", serviceName);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void testEntityNotFoundExceptionHandler() throws Exception {
        int status = HttpStatus.BAD_REQUEST.value();
        mockMvc.perform(get("/test").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.serviceName", is(serviceName)))
                .andExpect(jsonPath("$.errorCode", is(status)))
                .andExpect(jsonPath("$.globalMessage", is("Entity not found")));
    }

    @RestController
    static class TestController {
        @GetMapping("/test")
        public void throwException() {
            throw new EntityNotFoundException("Entity not found");
        }
    }
}

