package ru.otus.test;

import lombok.extern.slf4j.Slf4j;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class TestRunService {

    public static void main(String[] args) {
        run("ru.otus.test.TestMain");
    }

    public static void run(String testClassName) {
        try {
            Class<?> clazz = Class.forName(testClassName);
            List<Method> methods = Arrays.stream(clazz.getMethods()).collect(Collectors.toList());

            AtomicInteger exceptionCounter = new AtomicInteger();
            List<Method> tests = methods.stream().filter(method -> method.isAnnotationPresent(Test.class)).collect(Collectors.toList());
            tests.forEach(test -> {
                try {
                    Method before = methods.stream().filter(method -> method.isAnnotationPresent(Before.class)).findFirst().orElse(null);
                    Object testClass = clazz.getDeclaredConstructor().newInstance();
                    if (before != null) before.invoke(testClass);

                    try {
                        test.invoke(testClass);
                    } catch (Exception ex) {
                        exceptionCounter.getAndIncrement();
                    }

                    Method after = methods.stream().filter(method -> method.isAnnotationPresent(After.class)).findFirst().orElse(null);
                    if (after != null) after.invoke(testClass);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });

            log.info("{} Errors,{} Success", exceptionCounter.get(), tests.size()-exceptionCounter.get());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
