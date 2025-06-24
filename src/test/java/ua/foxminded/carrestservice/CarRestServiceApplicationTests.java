package ua.foxminded.carrestservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {CarRestServiceApplication.class})
@ActiveProfiles({"test"})
class CarRestServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
