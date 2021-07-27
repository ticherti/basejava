package com.ushine.webapp;

public class MainDeadLock {
    static final Object lock1 = "lock 1";
    static final Object lock2 = "lock 2";

    public static void main(String[] args) {
        new Thread(() -> lockThem(lock1, lock2)).start();
        new Thread(() -> lockThem(lock2, lock1)).start();
    }

    private static void lockThem(Object firstLock, Object secondLock) {
        synchronized (firstLock) {
            String name = Thread.currentThread().getName();
            System.out.println(name + " locked " + firstLock);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + "'s waiting for " + secondLock + " to be unlocked");
            synchronized (secondLock) {
                System.out.println(name + " " + secondLock);
            }
        }
    }
}
