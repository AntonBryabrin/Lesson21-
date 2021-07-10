package citrus.test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class FirstTest extends TestNGCitrusTestRunner {

    @Autowired
    private HttpClient restClient;
    private TestContext context;

    @Test(description = "Получение информации о юзере")
    @CitrusTest
    public void getTestsActions(){
        this.context = citrus.createTestContext();

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("users/2")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                .payload
                        ("{\n" +
                        "   \"data\":{\n" +
                        "      \"id\":2,\n" +
                        "      \"email\":\"janet.weaver@reqres.in\",\n" +
                        "      \"first_name\":\"Janet\",\n" +
                        "      \"last_name\":\"Weaver\",\n" +
                        "      \"avatar\":\"https://reqres.in/img/faces/2-image.jpg\"\n" +
                        "   },\n" +
                        "   \"support\":{\n" +
                        "      \"url\":\"https://reqres.in/#support-heading\",\n" +
                        "      \"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\"\n" +
                        "   }\n" +
                        "}")
        );



    }

    @Test(description = "Получение о нечуществующего юзера")
    @CitrusTest
    public void userNotFoundTest() {
        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("/users/23")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .statusCode(404)
                .payload("{}")
        );

    }

    @Test(description = "Удаление пользователя")
    @CitrusTest
    public void deleteUserTest() {
        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .delete("/users/2")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .statusCode(204)
        );

    }

    @Test(description = "Обновление информации о пользователе")
    @CitrusTest
    public void updateUserTest() {
        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .put("/users/2")
                .payload("{\n" +
                        "    \"name\": \"Mario\",\n" +
                        "    \"job\": \"Plumber\"\n" +
                        "}")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                //.status(OK)
                .payload("{\n" +
                        "    \"name\": \"Mario\",\n" +
                        "    \"job\": \"Plumber\",\n" +
                        "    \"updatedAt\": \"@assertThat(notNullValue())@\"\n" +
                        "}")
        );

    }


}
