package facades;

import dtos.ConferenceDTO;
import dtos.TalkDTO;
import entities.Conference;
import entities.Speaker;
import entities.Talk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.Collection;
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

    public List<TalkDTO> getAllTalksByConferenceId(String conferenceIdAsString) throws WebApplicationException {
        int conferenceId = Integer.parseInt(conferenceIdAsString);
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Talk> query = em.createQuery("SELECT t FROM Talk t where t.conference.id = :id", Talk.class);
            query.setParameter("id", conferenceId);
            List<Talk> talks = query.getResultList();
            System.out.println(talks.size());
            ArrayList<TalkDTO> talkDTOS = new ArrayList<>();
            for (Talk t : talks) {
                System.out.println(t.getId() + " " + t.getTopic());
                talkDTOS.add(new TalkDTO(t));
            }
            return talkDTOS;
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }
    public List<TalkDTO> getAllTalksBySpeakerId(String speakerIdAsString) throws WebApplicationException {
        int speakerId = Integer.parseInt(speakerIdAsString);
        EntityManager em = emf.createEntityManager();
        Speaker speaker;
        try {
            speaker = em.find(Speaker.class, speakerId);
            if (speaker == null) {
                throw new WebApplicationException("No speakers found");
            }
        } finally {
            em.close();
        }
        List<Talk> talks = speaker.getTalks();
        List<TalkDTO> talkDTOS = new ArrayList<>();
        for (Talk talk : talks) {
            TalkDTO talkDTO = new TalkDTO(talk);
            talkDTOS.add(talkDTO);
        }
        return talkDTOS;
    }
}
