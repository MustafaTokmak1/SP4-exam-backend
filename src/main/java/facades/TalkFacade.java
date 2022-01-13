package facades;

import dtos.ConferenceDTO;
import entities.Conference;

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
