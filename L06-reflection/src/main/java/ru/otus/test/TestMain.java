package ru.otus.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class TestMain {
    private static final Logger log = LoggerFactory.getLogger(TestMain.class);

    @Before
    public void setUp() {
        log.info("SET UP!");
    }

    @After
    public void tearDown() {
        log.info("TEAR DOWN!");
    }

    @Test
    public void test1() {
        log.info("TEST 1!");
        throw new RuntimeException("test1 exception");
    }

    @Test
    public void test2() {
        log.info("TEST 2!");
    }
}
