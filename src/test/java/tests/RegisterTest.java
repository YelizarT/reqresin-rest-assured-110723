package tests;

import dto.ErrorRegisterResponse;
import dto.RegisterRequest;
import dto.SuccessRegisterResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    @Test
    public void  successRegister () {
        RegisterRequest requestBody = new
                RegisterRequest("eve.holt@reqres.in", "pistol");
        //RegisterRequest request = RegisterRequest.builder()
        //.email("eve.holt@reqres.in").password("pistol").build(); // тоже самое, что две строчки выше, просто немного другой синтаксис
        SuccessRegisterResponse response = given().baseUri("https://reqres.in")
                .body(requestBody)
                .when().log().all()
                .contentType(ContentType.JSON)
                .post("/api/register")
                .then().log().all().statusCode(200)
                .extract().body().jsonPath().getObject("", SuccessRegisterResponse.class);

        System.out.println(response.getId());
        //Check that id, token are not empty
        assertNotNull(response.getId());
        assertFalse(response.getToken().isEmpty());
        //Check that id = 4
        assertEquals(4, response.getId());
    }

    @Test
    public void registerWithoutPassword() {
        RegisterRequest request = RegisterRequest.builder()
                .email("eve.holt@reqres.in").build();
        ErrorRegisterResponse response = given().baseUri("https://reqres.in")
                .body(request)
                .when().log().all()
                .contentType(ContentType.JSON)
                .post("/api/register")
                .then().log().all().statusCode(400)
                .extract().body().jsonPath().getObject("", ErrorRegisterResponse.class);
        //body contains "Missing password"
        assertEquals("Missing password", response.getError());
    }
    @Test
    public void registerWithoutEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .password("somePassword")
                .build(); // Отправляем запрос с пустым полем email

        ErrorRegisterResponse response = given().baseUri("https://reqres.in")
                .body(request)
                .when().log().all()
                .contentType(ContentType.JSON)
                .post("/api/register")
                .then().log().all().statusCode(400)
                .extract().as(ErrorRegisterResponse.class);
        // Проверяем, что в ответе есть сообщение об ошибке "Missing email or username"
        assertEquals("Missing email or username", response.getError());
    }

    @Test
    public void registerWithInvalidData() {
        RegisterRequest request = RegisterRequest.builder()
                .email("invalidEmail")
                .password("invalidPassword")
                .build();

        ErrorRegisterResponse response = given().baseUri("https://reqres.in")
                .body(request)
                .when().log().all()
                .contentType(ContentType.JSON)
                .post("/api/register")
                .then().log().all().statusCode(400)
                .extract().as(ErrorRegisterResponse.class);

        assertEquals("Note: Only defined users succeed registration", response.getError());
    }
}
