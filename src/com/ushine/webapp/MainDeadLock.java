package com.ushine.webapp;

public class MainDeadLock {
    static final Object lock1 = new Object();
    static final Object lock2 = new Object();

    public static void main(String[] args) {

        new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread() + " locks A");
                sleep();
                System.out.println("Waiting for B to unlock");
                synchronized (lock2) {
                    System.out.println(Thread.currentThread() + " locks B");
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread() + " locks B");
                sleep();
                System.out.println("Waiting for A to unlock");
                synchronized (lock1) {
                    System.out.println(Thread.currentThread() + " locks A");
                }
            }
        }).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
