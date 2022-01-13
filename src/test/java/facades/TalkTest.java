package facades;

import entities.Conference;
import entities.Speaker;
import entities.Talk;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

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

            c1.addTalk(t1);
            c2.addTalk(t2);
            em.persist(t1);
            em.persist(t2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }/*
    @Test
    //us1
    public void testGetAllConferences() {
        assertEquals(2, facade.getAllConferences().size(), "Expects two rows in the database");
    }
    */
}

