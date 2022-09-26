package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePingPong {
    private static final Logger logger = LoggerFactory.getLogger(HomePingPong.class);
    private static int counter = 0;
    private static boolean done = false;
    private static boolean isOperatorPlus = true;

    private synchronized void action(int c) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                while (done) {
                    done = false;
                    logger.info("----> {} - waiting", Thread.currentThread().getName());
                    this.wait();
                }

                if (counter == 1) {
                    isOperatorPlus = true;
                }
                if (counter == 10) {
                    isOperatorPlus = false;
                }

                counter = isOperatorPlus ? counter + c : counter - c;

                sleep();

                done = true;
                notifyAll();

                logger.info("----> {} - counter: {} - after notify", Thread.currentThread().getName(), counter);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        HomePingPong homePingPong = new HomePingPong();

        Thread thread1 = new Thread(() -> homePingPong.action(1));
        Thread thread2 = new Thread(() -> homePingPong.action(0));

        thread1.setName("First");
        thread2.setName("Second");

        thread1.start();
        while (true) {
            if (thread1.isAlive()) {
                thread2.start();
                break;
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
