package hw;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestProcessor {

  /**
   * Данный метод находит все void методы без аргументов в классе, и запускеет их.
   * <p>
   * Для запуска создается тестовый объект с помощью конструткора без аргументов.
   */
  public static void runTest(Class<?> testClass) {
    final Constructor<?> declaredConstructor;
    try {
      declaredConstructor = testClass.getDeclaredConstructor();
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(
          "Для класса \"" + testClass.getName() + "\" не найден конструктор без аргументов");
    }

    final Object testObj;
    try {
      testObj = declaredConstructor.newInstance();
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(
          "Не удалось создать объект класса \"" + testClass.getName() + "\"");
    }

    List<Method> methods = new ArrayList<>();
    for (Method method : testClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Test.class)) {
        checkTestMethod(method);
        methods.add(method);
      }
    }

    // Добавляем сортировку тестов по порядку
    methods.sort(Comparator.comparingInt(m -> m.getAnnotation(Test.class).order()));

    methods.forEach(it -> runTest(it, testObj));
  }

  private static void checkTestMethod(Method method) {
    if (!method.getReturnType().isAssignableFrom(void.class) || method.getParameterCount() != 0) {
      throw new IllegalArgumentException(
          "Метод \"" + method.getName() + "\" должен быть void и не иметь аргументов");
    }

    if (method.isAnnotationPresent(Skip.class)) {
      throw new IllegalArgumentException(
          "Тест \"" + method.getName() + "\" помечен аннотацией @Skip и не будет запущен");
    }
  }

  private static void runTest(Method testMethod, Object testObj) {
    if (testMethod.isAnnotationPresent(BeforeEach.class)) {
      invokeMethod(testMethod, testObj);
    }

    try {
      invokeMethod(testMethod, testObj);
    } catch (AssertionError e) {
      // Распечатываем причину, по которой тест провалился
      e.printStackTrace();
      throw new RuntimeException(
          "Тест \"" + testMethod.getName() + "\" провалился: " + e.getMessage(), e);
    }

    if (testMethod.isAnnotationPresent(AfterEach.class)) {
      invokeMethod(testMethod, testObj);
    }
  }


  private static void invokeMethod(Method method, Object obj) {
    try {
      method.invoke(obj);
    } catch (InvocationTargetException | IllegalAccessException e) {
      // handle exceptions
    }
  }

}