package com.ushine.webapp.storage;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ArrayStorageTest extends AbstractArrayStorageTest {

    @Before
    public void setUp() throws Exception{
        storage = new ArrayStorage();
        super.setUp();
    }

    @Test
    public void getIndex() throws InvocationTargetException, IllegalAccessException {
        Method[] methods = storage.getClass().getDeclaredMethods();
        int result = - 999;
        for (Method m: methods) {
            if (m.getName().equals("getIndex")){
                result = (int) m.invoke(storage, "uuid3");
            }
        }
        assertEquals(2, result);
    }

    @Test
    public void getNegativeIndex() throws InvocationTargetException, IllegalAccessException {
        Method[] methods = storage.getClass().getDeclaredMethods();
        int result = - 999;
        for (Method m: methods) {
            if (m.getName().equals("getIndex")){
                result = (int) m.invoke(storage, "dummy");
            }
        }
        assertEquals(-1, result);
    }

    @Test
    public void insert() {

    }

    @Test
    public void takeOut() {

    }
}