package ut.fi.ambientia.mocks;

import static org.mockito.Mockito.mock;

public interface Dummy {
    static <T> T dummy(Class<T> klazz) {
        return mock(klazz);
    }
}
