package ru.otus.handler;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
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
        when(dateTimeProvider.currentTimeMillis()).thenReturn(System.currentTimeMillis());

        //given
        var testMessage = new Message.Builder(1L)
                .field1("field1")
                .build();

        List<Processor> processors = List.of(new ProcessorEvenSecondCheck());
        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        Executable process = () -> {
            long startTime = dateTimeProvider.currentTimeMillis();

            while (true) {
                try {
                    complexProcessor.handle(testMessage);
                } catch (TestException exception) {
                    break;
                }
                // Предохранитель, чтобы в бесконечный цикл не уйти, тест упадет после выхода.
                if (dateTimeProvider.currentTimeMillis() - startTime > 10000) {
                    return;
                }
            }
            //вызовется только после того как упадет обработчик, асерт поймает его, тест будет пройден.
            throw new TestException("");
        };

        //then
        assertThrows(TestException.class, process);

    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}