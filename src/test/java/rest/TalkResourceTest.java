package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.SpeakerDTO;
import dtos.TalkDTO;
import entities.Conference;
import entities.RenameMe;
import entities.Speaker;
import entities.Talk;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class TalkResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private Talk t1 = new Talk("Soccer", 90);
    private Talk t2 = new Talk("Basket", 90);
    private Conference c1 = new Conference("ESB", "Testroad 23", 2000);
    private Conference c2 = new Conference("Walt Disney Conference Center", "Testway 31", 3200);
    private Speaker s1 = new Speaker("Charles", "SoccerPlayer", "male");
    private Speaker s2 = new Speaker("Kevin", "BasketballPlayer", "male");
    private Speaker s3 = new Speaker("David", "Health Specialist", "male");

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
        em.createNamedQuery("Talk.deleteAllRows").executeUpdate();
        em.createNamedQuery("Conference.deleteAllRows").executeUpdate();
        em.createNamedQuery("Speaker.deleteAllRows").executeUpdate();

        em.persist(t1);
        em.persist(t2);


        s1.addTalkToSpeaker(t1);
        s2.addTalkToSpeaker(t2);
        s3.addTalkToSpeaker(t1);
        s3.addTalkToSpeaker(t2);

        c1.addTalk(t1);
        c2.addTalk(t2);
        em.persist(t1);
        em.persist(t2);
        em.persist(s1);
        em.persist(s2);
        em.persist(c1);
        em.persist(c2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/xxx").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/xxx/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World"));
    }

    @Test
    public void testCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/xxx/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(0));
    }
    @Test
    public void testAllConferencesEndpoint() {
        given()
                .contentType("application/json")
                .get("/talk/conferences").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name",hasItems("ESB", "Walt Disney Conference Center") );
    }
    @Test
    public void printJSON() {
        given().log().all().when().get("/talk/conferences").then().log().body();
    }
    @Test
    public void testTalkSize() {
        given()
                .contentType("application/json")
                .get("/talk/talks").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(2))
                .body("", hasSize(2))
                .body("$", hasSize(2));
        ;
    }
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testCreateSpeakerEndpoint() {
        Speaker s4 = new Speaker("Speaker 4","Speaker 4","male");
        SpeakerDTO speakerDTO = new SpeakerDTO(s4);
        String requestBody = GSON.toJson(speakerDTO);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/talk/createSpeaker")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Speaker 4", response.jsonPath().getString("name"));
    }

}
