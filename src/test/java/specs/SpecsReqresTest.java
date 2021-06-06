package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.with;
import static java.sql.Types.NULL;
import static org.hamcrest.Matchers.not;

public class SpecsReqresTest {
    public static RequestSpecification request = with()
            .filter(customLogFilter().withCustomTemplates())
            .baseUri("https://reqres.in")
            .basePath("/api")
            //.log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(not(NULL))
            .build();
}
