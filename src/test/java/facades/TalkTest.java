package facades;

import dtos.TalkDTO;
import entities.Conference;
import entities.Speaker;
import entities.Talk;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import entities.RenameMe;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class TalkTest {

    private static EntityManagerFactory emf;
    private static TalkFacade facade;
    private Talk t1 = new Talk("Soccer", 90);
    private Talk t2 = new Talk("Basket", 90);
    private Conference c1 = new Conference("ESB", "Testroad 23", 2000);
    private Conference c2 = new Conference("Walt Disney Conference Center", "Testway 31", 3200);
    private Speaker s1 = new Speaker("Charles", "SoccerPlayer", "male");
    private Speaker s2 = new Speaker("Kevin", "BasketballPlayer", "male");
    private Speaker s3 = new Speaker("David", "Health Specialist", "male");

    public TalkTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = TalkFacade.getFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
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
            //c1.addTalk(t2); test af us7
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

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    //US1
    public void testGetAllConferences() throws Exception {
        assertEquals(2, facade.getAllConferences().size(), "Expects two rows in the database");
    }

    @Test
    //US2
    public void testGetAllTalksByConferenceId() {
        String id = String.valueOf(c1.getId());
        assertEquals(1, facade.getAllTalksByConferenceId(id).size(), "Expects one rows in the database");
    }

    @Test
    //US3
    public void testGetAllTalksBySpeaker() {
        String id = String.valueOf(s3.getId());
        assertEquals(2, facade.getAllTalksBySpeakerId(id).size(), "Expects one rows in the database");
    }
    /*
    @Test
    //US7
    public void testDeleteTalk() {
        TalkDTO talkDTO = facade.deleteTalk(t2.getId());
        assertEquals(1, facade.getAllTalksByConferenceId(String.valueOf(c1.getId())).size(), "Expects one rows in the database");
    }

     */

}
