package org.gaoxinbo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MainTest {

    static class Unsafe {
        private int value;

        public int getNext() {
            return ++value;
        }

        public int getValue() {
            return value;
        }
    }

    static class Safe {
        private int value;

        public synchronized int getNext() {
            return ++value;
        }

        public int getValue() {
            return value;
        }
    }

    @RepeatedTest(10)
    @Test
    void safeTest() throws InterruptedException {
        Safe safe = new Safe();
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int count = 0; count < 10; count++) {
            executorService.submit(() -> {
                System.out.println("safe " + safe.getNext());
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        System.out.println("safe value: " + safe.getValue());

        Assertions.assertEquals(10, safe.getValue());
    }

    @RepeatedTest(10)
    @Test
    void unsafeTest() throws InterruptedException {
        Unsafe unsafe = new Unsafe();
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int count = 0; count < 10; count++) {
            executorService.submit(() -> {
                System.out.println("unsafe " + unsafe.getNext());
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        System.out.println("unsafe value: " + unsafe.getValue());

        Assertions.assertEquals(10, unsafe.getValue());
    }


}