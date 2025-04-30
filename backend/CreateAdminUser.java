import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class CreateAdminUser {
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws Exception {
        // First, create a position
        String positionJson = "{\"position\": \"ADMINISTRATOR\"}";
        HttpRequest positionRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/positions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(positionJson, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> positionResponse = client.send(positionRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Position creation response: " + positionResponse.statusCode());
            System.out.println(positionResponse.body());
        } catch (Exception e) {
            System.out.println("Error creating position: " + e.getMessage());
        }

        // Now try to create a user
        String userJson = "{" +
                "\"email\": \"madhavam@test.com\"," +
                "\"firstName\": \"Madhavam\"," +
                "\"lastName\": \"Shahi\"," +
                "\"password\": \"123456\"," +
                "\"phoneNumber\": \"014-988-1942\"," +
                "\"role\": \"ADMIN\"," +
                "\"position\": [\"ADMINISTRATOR\"]" +
                "}";

        HttpRequest userRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/crewMember"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(userJson, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> userResponse = client.send(userRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("\nUser creation response: " + userResponse.statusCode());
            System.out.println(userResponse.body());
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }
}