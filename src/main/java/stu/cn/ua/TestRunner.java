package stu.cn.ua;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) throws ClassNotFoundException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Exactly 1 argument must be provided");
        }

        String className = args[0];
        Class<?> testClass = Class.forName(className);
        System.out.println("Class " + testClass.getName() + " successfully loaded");

        // Отримуємо всі методи класу
        Method[] methods = testClass.getDeclaredMethods();

        int totalMethods = methods.length;
        int totalTests = 0;
        int successfulTests = 0;
        int failedTests = 0;

        List<String> report = new ArrayList<>();

        for (Method method : methods) {
            // Перевіряємо відповідність тестовим критеріям
            boolean isTestMethod = method.getName().startsWith("test") &&
                    method.getReturnType().equals(void.class) &&
                    method.getParameterCount() == 0 &&
                    method.getModifiers() == java.lang.reflect.Modifier.PUBLIC;

            // Якщо метод не відповідає критеріям, але починається на "test"
            if (!isTestMethod) {
                report.add("Method: " + method.getName() + " is not test method");
            }

            // Викликати цей метод як тестовий
            totalTests++;
            try {
                Object instance = testClass.getDeclaredConstructor().newInstance();
                method.invoke(instance);
                report.add("Test: " + method.getName() + " SUCCESSFUL");
                successfulTests++;
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                report.add("Test: " + method.getName() + " FAILED, error: " + cause.getMessage());
                failedTests++;
            } catch (Exception e) {
                report.add("Test: " + method.getName() + " FAILED, error: " + e.getMessage());
                failedTests++;
            }
        }

        // Виведення результатів
        for (String line : report) {
            System.out.println(line);
        }

        System.out.println("Total methods: " + totalMethods +
                " Total tests: " + totalTests +
                " Successful tests: " + successfulTests +
                " Failed tests: " + failedTests);
    }
}