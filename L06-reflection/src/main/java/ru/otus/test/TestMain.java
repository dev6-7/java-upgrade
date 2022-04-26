package ru.otus.test;

import lombok.extern.slf4j.Slf4j;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

@Slf4j
public class TestMain {

    @Before
    public void setUp(){
        log.info("SET UP!");
    }

    @After
    public void tearDown(){
        log.info("TEAR DOWN!");
    }

    @Test
    public void test1(){
        log.info("TEST 1!");
        throw new RuntimeException("test1 exception");
    }

    @Test
    public void test2(){
        log.info("TEST 2!");
    }
}
