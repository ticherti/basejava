package com.ushine.webapp;

import com.ushine.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
        Resume r = new Resume("Fullname");
        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new_uuid");
        // toString through reflexion. Perversity.
        Method[] mas = r.getClass().getDeclaredMethods();
        for (Method m : mas) {
            if (m.getName().equals("toString")) {
                System.out.println(m.invoke(r));
                break;
            }
        }
        System.out.println(r);
    }
}