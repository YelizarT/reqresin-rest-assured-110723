package tests;

import dto.UserDataResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetListUsers {
    @Test
    public void getListUserPage2() {
        List<UserDataResponse> users = given().baseUri("https://reqres.in")
                .when().log().all()
                .get("/api/users?page=2")
                .then().log().all().statusCode(200)
                .extract().body().jsonPath()
                .getList("data", UserDataResponse.class);
        System.out.println(users.get(0).getEmail());
        //Check that 6 items are in list
        assertEquals(6, users.size());
        //Check that each id from the list is positive value
        for (UserDataResponse user : users) {
            assertTrue(user.getId() > 0);
        //check that each user email ends with "@reqres.in"
            assertTrue(user.getEmail().endsWith("@reqres.in"));
        //Check that each avatar ends with "id-value" + "-image.jpg"
            assertTrue(user.getAvatar().endsWith(user.getId() + "-image.jpg"));
        }
        users.forEach(user -> assertTrue(user.getId()>0));
        users.forEach(user -> assertTrue(user.getEmail().endsWith("@reqres.in")));
        users.forEach(user -> assertTrue(user.getAvatar().endsWith(user.getId() + "-image.jpg")));
    }
}
