import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.Root;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class ReqresTestsWithoutSpecs {

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "https://reqres.in";
  }

  @Test
  void singleUserTest() {
    given().when()
        .filter(new AllureRestAssured()) // Обычное подключение Allure
        .get("/api/users/5")
        .then()
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
  void userWithEmailDomainExist() {
    given()
            .when()
            .get("/api/users")
            .then()
            .statusCode(200)
            .body(String.format("data.findAll{it.email =~/.*?@%s/}.email.flatten()", "reqres.in"), is(not(empty())));
  }

  @Test
  void specsTest() {
    given()
            .when()
            .get("/api/users")
            .then()
            .statusCode(200)
            .body("data", hasSize(greaterThan(0)))
            .log().body();
  }

  @Test
  void deleteUserTest() {
    given()
        .filter(customLogFilter().withCustomTemplates()) // Подключение Allure с кастомным фильтром
        .contentType(ContentType.JSON)
        .when()
        .delete("/api/users/5")
        .then()
        .statusCode(204);
  }

  @Test
  void createUserTest() {
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

    given().contentType(ContentType.JSON)
        .body("{ \"name\": \"mohen\", " +
                " \"job\": \"qa-engineer\" }")
        .when()
        .post("/api/users")
        .then()
        .statusCode(201)
        .body("name", is("mohen"))
        .body("job", is("qa-engineer"))
        .body("id",not(nullValue()))
        .body("createdAt", containsString(currentDate));
  }

  @Test
  void createUserTestWithHashMap() {
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

    Map<String, String> data = new HashMap<>();
    data.put("name", "mohen");
    data.put("job", "qa-engineer");

    given()
        .contentType(ContentType.JSON)
        .body(data)
        .when()
        .post("/api/users")
        .then()
        .statusCode(201)
        .body("name", is("mohen"))
        .body("job", is("qa-engineer"))
        .body("id",not(nullValue()))
        .body("createdAt", containsString(currentDate));
  }

  @Test
  void updateUserTest() {
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

    given().contentType(ContentType.JSON)
        .body("{ \"name\": \"mohen\", " +
              " \"job\": \"qa-engineer\" }")
        .when()
        .put("/api/users/5")
        .then()
        .statusCode(200)
        .body("name", is("mohen"))
        .body("job", is("qa-engineer"))
        .body("updatedAt", containsString(currentDate));
  }

  @Test
  void loginUserTest() {

    given().contentType(ContentType.JSON)
        .body("{ \"email\": \"eve.holt@reqres.in\", " +
              " \"password\": \"cityslicka\" }")
        .when()
        .post("/api/login/")
        .then()
        .statusCode(200)
        .body("token", is("QpwL5tke4Pnpja7X4"));
  }

  @Test
  void negativeLoginTest() {
    given().contentType(ContentType.JSON)
        .body("{ \"email\": \"xxx@mail.ru\"}")
        .when()
        .post("/api/login")
        .then()
        .statusCode(400)
        .body("error", is("Missing password"));
  }

  @Test
  @DisplayName("Соответсиве списка пользователей JSON схеме")
  void userListJsonShemaTest() {
    given()
        .filter(customLogFilter().withCustomTemplates())
        .contentType(ContentType.JSON)
        .get("/api/users/")
        .then()
        .body(matchesJsonSchemaInClasspath("jsonshemas/user_list_response.json"));
  }

  @Test
  @DisplayName("Соответсиве пользователя JSON модели")
  void userWithModelTest() {
    Root root =
        given()
            .filter(customLogFilter().withCustomTemplates())
            .contentType(JSON)
            .log().uri()
            .log().body()
            .get("/api/users/2")
            .then()
            .log().body()
            .extract().as(Root.class);

    assertThat(root.getData().getFirst_name()).isEqualTo("Janet");
    assertThat(root.getData().getLast_name()).isEqualTo("Weaver");
  }

}
