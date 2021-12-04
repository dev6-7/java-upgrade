package ru.otus.handler;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.ProcessorEvenSecondCheck;
import ru.otus.provider.DateTimeProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EvenSecondProcessorTest {

    @Test
    @DisplayName("Тестируем обработку исключения процессора ProcessorEvenSecondCheck")
    void ProcessorEvenSecondCheckTest() {
        var dateTimeProvider = mock(DateTimeProvider.class);
        when(dateTimeProvider.getSecond()).thenReturn(2L);

        //given
        var testMessage = new Message.Builder(1L)
                .field1("field1")
                .build();

        List<Processor> processors = List.of(new ProcessorEvenSecondCheck(dateTimeProvider));
        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        //then
        assertThrows(TestException.class, () -> complexProcessor.handle(testMessage));
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}