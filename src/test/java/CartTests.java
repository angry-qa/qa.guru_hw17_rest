import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

public class CartTests {

  private static final String contentType = "application/x-www-form-urlencoded; charset=UTF-8";

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "http://demowebshop.tricentis.com";
    Configuration.baseUrl = "http://demowebshop.tricentis.com";
    Configuration.startMaximized = true;
  }

   String getCookie() {
     return given()
        .contentType(contentType)
        .when()
        .post("/addproducttocart/catalog/13/1/1")
        .then()
        .body("success", is(true))
        .extract().cookie("Nop.customer").toString();
  }

  @Test
  void checkCountByApi() {
    given()
        .contentType(contentType)
        .cookie("Nop.customer", getCookie())
        .when()
        .post("/addproducttocart/catalog/13/1/1")
        .then()
        .statusCode(200)
        .body("success", is(true))
        .body("updatetopcartsectionhtml", is("(2)"));
  }

  @Test
  void checkCountByUI() {
    open("/books");

    Cookie ck = new Cookie("Nop.customer", getCookie());
    WebDriverRunner.getWebDriver().manage().addCookie(ck);

    Selenide.refresh();
    $("#topcartlink .ico-cart").shouldHave(text("(1)"));
  }

}
