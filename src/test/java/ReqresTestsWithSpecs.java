import models.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.SpecsReqresTest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class ReqresTestsWithSpecs {

  @Test
  @DisplayName("Получение пользователя по id")
  void singleUserTest() {
    SpecsReqresTest.request
        //.filter(new AllureRestAssured()) // Обычное подключение Allure
        .get("/users/5")
        .then()
        .spec(SpecsReqresTest.responseSpec)
        .statusCode(200)
        .body("data.id", is(5))
        .body("data.email", is("charles.morris@reqres.in"))
        .body("data.first_name", is("Charles"))
        .body("data.last_name", is("Morris"))
        .body("data.avatar", is("https://reqres.in/img/faces/5-image.jpg"))
        .body("support.url", is("https://reqres.in/#support-heading"))
        .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
  }

  @Test
  @DisplayName("Существует пользователь с доменом reqres.in (Groovy)")
  void userWithEmailDomainExist() {
    SpecsReqresTest.request
            .get("/users")
            .then()
            .spec(SpecsReqresTest.responseSpec)
            .statusCode(200)
            .body(String.format("data.findAll{it.email =~/.*?@%s/}.email.flatten()", "reqres.in"), is(not(empty())));
  }

  @Test
  @DisplayName("Проверка что список пользователей не пустой")
  void specsTest() {
    SpecsReqresTest.request
            .when()
            .get("/users")
            .then()
            .spec(SpecsReqresTest.responseSpec)
            .statusCode(200)
            .body("data", hasSize(greaterThan(0)))
            .log().body();
  }

  @Test
  @DisplayName("Удаление пользователя")
  void deleteUserTest() {
    SpecsReqresTest.request
        //.filter(customLogFilter().withCustomTemplates()) // Подключение Allure с кастомным фильтром
        .when()
        .delete("/users/5")
        .then()
        .spec(SpecsReqresTest.responseSpec)
        .statusCode(204);
  }

  @Test
  @DisplayName("Создание пользователя")
  void createUserTest() {
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

    SpecsReqresTest.request
        .body("{ \"name\": \"mohen\", " +
                " \"job\": \"qa-engineer\" }")
        .when()
        .post("/users")
        .then()
        .spec(SpecsReqresTest.responseSpec)
        .statusCode(201)
        .body("name", is("mohen"))
        .body("job", is("qa-engineer"))
        .body("id",not(nullValue()))
        .body("createdAt", containsString(currentDate));
  }

  @Test
  @DisplayName("Создание пользователя с HashMap")
  void createUserTestWithHashMap() {
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

    Map<String, String> data = new HashMap<>();
    data.put("name", "mohen");
    data.put("job", "qa-engineer");

    SpecsReqresTest.request
        .body(data)
        .when()
        .post("/users")
        .then()
        .spec(SpecsReqresTest.responseSpec)
        .statusCode(201)
        .body("name", is("mohen"))
        .body("job", is("qa-engineer"))
        .body("id",not(nullValue()))
        .body("createdAt", containsString(currentDate));
  }

  @Test
  @DisplayName("Обновление данных о пользователе")
  void updateUserTest() {
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

    SpecsReqresTest.request
        .body("{ \"name\": \"mohen\", " +
              " \"job\": \"qa-engineer\" }")
        .when()
        .put("/users/5")
        .then()
        .spec(SpecsReqresTest.responseSpec)
        .statusCode(200)
        .body("name", is("mohen"))
        .body("job", is("qa-engineer"))
        .body("updatedAt", containsString(currentDate));
  }

  @Test
  @DisplayName("Логин с валидными данными")
  void loginUserTest() {
    SpecsReqresTest.request
        .body("{ \"email\": \"eve.holt@reqres.in\", " +
              " \"password\": \"cityslicka\" }")
        .when()
        .post("/login/")
        .then()
        .spec(SpecsReqresTest.responseSpec)
        .statusCode(200)
        .body("token", is("QpwL5tke4Pnpja7X4"));
  }

  @Test
  @DisplayName("Логин с невалидными данными")
  void negativeLoginTest() {
    SpecsReqresTest.request
        .body("{ \"email\": \"xxx@mail.ru\"}")
        .when()
        .post("/login")
        .then()
        .spec(SpecsReqresTest.responseSpec)
        .statusCode(400)
        .body("error", is("Missing password"));
  }

  @Test
  @DisplayName("Соответсиве списка пользователей JSON схеме")
  void userListJsonShemaTest() {
    SpecsReqresTest.request
        //.filter(customLogFilter().withCustomTemplates())
        .get("/users/")
        .then()
        .spec(SpecsReqresTest.responseSpec)
        .body(matchesJsonSchemaInClasspath("jsonshemas/user_list_response.json"));
  }

  @Test
  @DisplayName("Соответсиве пользователя JSON модели")
  void userWithModelTest() {
    Root root =
            SpecsReqresTest.request
            //.filter(customLogFilter().withCustomTemplates())
            .log().uri()
            .log().body()
            .get("/users/2")
            .then()
            .spec(SpecsReqresTest.responseSpec)
            .log().body()
            .extract().as(Root.class);

    assertThat(root.getData().getFirst_name()).isEqualTo("Janet");
    assertThat(root.getData().getLast_name()).isEqualTo("Weaver");
  }

}
