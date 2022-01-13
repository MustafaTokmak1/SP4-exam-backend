package facades;

import dtos.ConferenceDTO;
import entities.Conference;
import entities.Speaker;
import entities.Talk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

public class TalkFacade {
    private static TalkFacade instance;
    private static EntityManagerFactory emf;

    private TalkFacade() {
    }
    public static TalkFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TalkFacade();
        }
        return instance;
    }
/*
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }*/

    public void populateDB(){
            EntityManager em = emf.createEntityManager();
       Talk t1 = new Talk("Soccer", 90);
       Talk t2 = new Talk("Basket", 90);
       Conference c1 = new Conference("ESB", "Testroad 23", 2000);
       Conference c2 = new Conference("Walt Disney Conference Center", "Testway 31", 3200);
       Speaker s1 = new Speaker("Charles", "SoccerPlayer", "male");
       Speaker s2 = new Speaker("Kevin", "BasketballPlayer", "male");
       Speaker s3 = new Speaker("David", "Health Specialist", "male");
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
                em.persist(s1);
                em.persist(s2);
                em.persist(c1);
                em.persist(c2);

                em.getTransaction().commit();
            } finally {
                em.close();
            }
        }


    public List<ConferenceDTO> getAllConferences(){
        EntityManager em = emf.createEntityManager();
        List<Conference> conferences = em.createQuery("select c FROM Conference c", Conference.class).getResultList();
        if (conferences == null){
            throw new WebApplicationException("No conferences were found",404);
        }
        List<ConferenceDTO> conferenceDTOS = new ArrayList<>();
        for (Conference currentConference : conferences) {
            conferenceDTOS.add(new ConferenceDTO(currentConference));
        }
        return conferenceDTOS;
    }
}
