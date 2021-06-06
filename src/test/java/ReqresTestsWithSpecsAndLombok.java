import modelslombok.Root;
import modelslombok.User;
import modelslombok.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.SpecsReqresTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTestsWithSpecsAndLombok {

  @Test
  @DisplayName("Проврека JSON с одной нодой")
  void singleUserWithLombokModelOneNode() {
    // @formatter:off
    UserData data = SpecsReqresTest.request
            .when()
                .get("/users/2")
            .then()
              .spec(SpecsReqresTest.responseSpec)
              .log().body()
              .extract().as(UserData.class);
    // @formatter:on

    assertEquals(2, data.getUser().getId());
  }

  @Test
  @DisplayName("Проврека JSON с несколькими нодами")
  void singleUserWithLombokModelManyNode() {
    // @formatter:off
    Root root =
            SpecsReqresTest.request
                    .log().uri()
                    .log().body()
                        .get("/users/2")
                    .then()
                        .spec(SpecsReqresTest.responseSpec)
                        .log().body()
                        .extract().as(Root.class);
    // @formatter:on

    assertThat(root.getData().getFirstName()).isEqualTo("Janet");
    assertThat(root.getData().getLastName()).isEqualTo("Weaver");
    assertThat(root.getData().getAvatar()).isEqualTo("https://reqres.in/img/faces/2-image.jpg");

    assertThat(root.getSupport().getText()).isEqualTo("To keep ReqRes free, contributions towards server costs are appreciated!");
  }

  @Test
  @DisplayName("Содание объекта билдером")
  // TODO нужно найти как этот созданный объект можно использовать
  void singleUserWithLombokModel() {

    User user = User.builder().id(50).email("chuk.norris@reqres.in").firstName("Chuk").lastName("Norris").build();
    assertThat(user.getEmail()).isEqualTo("chuk.norris@reqres.in");
  }

}
