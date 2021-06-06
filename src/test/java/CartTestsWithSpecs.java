import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import specs.SpecsCartTest;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static filters.CustomLogFilter.customLogFilter;
import static org.hamcrest.Matchers.is;

class CartTestsWithSpecs {

  @BeforeAll
  static void setup() {
    Configuration.baseUrl = "http://demowebshop.tricentis.com";
    Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub/";
    Configuration.startMaximized = true;
  }

   String getCookie() {
     return SpecsCartTest.request
         .filter(customLogFilter().withCustomTemplates())
         .when()
         .post("/addproducttocart/catalog/13/1/1")
         .then()
             .spec(SpecsCartTest.responseSpec)
         .extract().cookie("Nop.customer").toString();
  }

  @Test
  @DisplayName("Проверка количества в заказе по API")
  void checkCountByApi() {
    SpecsCartTest.request
        .filter(customLogFilter().withCustomTemplates())
        .cookie("Nop.customer", getCookie())
        .when()
        .post("/addproducttocart/catalog/13/1/1")
        .then()
            .spec(SpecsCartTest.responseSpec)
        .body("updatetopcartsectionhtml", is("(2)"));
  }

  @Test
  @DisplayName("Проверка количества в заказе по UI")
  void checkCountByUI() {
    open("/books");

    Cookie ck = new Cookie("Nop.customer", getCookie());
    WebDriverRunner.getWebDriver().manage().addCookie(ck);

    Selenide.refresh();
    $("#topcartlink .ico-cart").shouldHave(text("(1)"));
  }

}
