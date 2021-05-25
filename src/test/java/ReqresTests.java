import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReqresTests {

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "https://reqres.in";
  }

  @Test
  public void singleUserTest() {
    given().when()
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
  public void deleteUserTest() {
    given().contentType(ContentType.JSON)
        .when()
        .delete("/api/users/5")
        .then()
        .statusCode(204);
  }

  @Test
  public void createUserTest() {
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
  public void updateUserTest() {
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
  public void loginUserTest() {

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
  public void negativeLoginTest() {
    given().contentType(ContentType.JSON)
        .body("{ \"email\": \"xxx@mail.ru\"}")
        .when()
        .post("/api/login")
        .then()
        .statusCode(400)
        .body("error", is("Missing password"));
  }

}
