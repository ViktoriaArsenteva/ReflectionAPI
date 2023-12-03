package hw;


public class hw {

  /**
   * Расширить пример с запуском тестов следующими фичами: 
   * 1. Добавить аннотации BeforeEach, AfterEach, которые ставятся над методами void без аругментов и запускаются ДО и ПОСЛЕ всех тестов соответственно. 
   * 2. В аннотацию Test добавить параметр order() со значением 0 по
   * умолчанию. Необходимо при запуске тестов фильтровать тесты по этому параметру (от меньшего к
   * большему). Т.е. если есть методы @Test(order = -2) void first, @Test void second, Test(order =
   * 5) void third, то порядок вызовов first -> second -> third 
   * 3.* Добавить аннотацию @Skip, которую можно ставить над тест-методами. Если она стоит - то тест не запускается.
   * 4.* При наличии идей, реализовать их и написать об этом в комментарии при сдаче.
   */


  public static void main(String[] args) {
    TestProcessor.runTest(MyTest.class);
  }

  static class MyTest {

    @Test(order = 1)
    void firstTest() {
      System.out.println("firstTest запущен");
    }

    @Test(order = 2)
    void secondTest() {
      System.out.println("secondTest запущен");
    }

    @Test(order = 3)
    void thirdTest() {
      System.out.println("thirdTest запущен");
    }

    @BeforeEach
    void beforeEach() {
      System.out.println("BeforeEach метод");
    }

    @AfterEach
    void afterEach() {
      System.out.println("AfterEach метод");
    }

  }


}