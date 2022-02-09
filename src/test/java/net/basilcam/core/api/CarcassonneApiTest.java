package net.basilcam.core.api;

import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;

class CarcassonneApiTest {
    public CarcassonneApi api;
    public CarcassonneHandler handler = mock(CarcassonneHandler.class);

    @BeforeEach
    public void beforeEach() {
        api = new CarcassonneApi();
        api.register(handler);
    }
}