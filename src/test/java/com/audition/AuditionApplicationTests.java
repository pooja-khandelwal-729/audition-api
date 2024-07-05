package com.audition;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class AuditionApplicationTests {

    @Test
    void contextLoads(final ApplicationContext ctx) {
        assertThat(ctx).isNotNull();
    }

}
