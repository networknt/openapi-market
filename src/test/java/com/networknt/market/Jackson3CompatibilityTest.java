package com.networknt.market;

import com.networknt.config.Config;
import com.networknt.market.model.Product;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Jackson3CompatibilityTest {
    private final ObjectMapper mapper = Config.getInstance().getMapper();

    @Test
    void generatedModelRoundTripsWithFrameworkMapper() {
        Product value = mapper.readValue("{\"id\":7,\"name\":\"café\",\"tag\":\"fixture\"}", Product.class);
        assertEquals(7L, value.getId());
        assertEquals("café", value.getName());
        assertEquals(value, mapper.readValue(mapper.writeValueAsBytes(value), Product.class));
    }

    @Test
    void runtimeDoesNotContainJackson2Databind() {
        assertThrows(ClassNotFoundException.class, () -> Class.forName("com.fasterxml.jackson.databind.ObjectMapper"));
    }

    @Test
    void malformedModelJsonIsRejected() {
        assertThrows(tools.jackson.core.JacksonException.class, () -> mapper.readValue("{broken", Product.class));
    }
}
