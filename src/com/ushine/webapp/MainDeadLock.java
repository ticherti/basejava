package com.ushine.webapp;

public class MainDeadLock {
    private static final Object LOCK_1 = "lock 1";
    private static final Object LOCK_2 = "lock 2";

    public static void main(String[] args) {
        new Thread(() -> lockThem(LOCK_1, LOCK_2)).start();
        new Thread(() -> lockThem(LOCK_2, LOCK_1)).start();
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
