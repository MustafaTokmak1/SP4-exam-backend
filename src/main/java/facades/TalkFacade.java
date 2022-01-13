package facades;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.ConferenceDTO;
import dtos.SpeakerDTO;
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
    public TalkDTO createTalk(String talkJSON) {
        EntityManager em = emf.createEntityManager();
        String topic;
        int duration;
        try {
            JsonObject json = JsonParser.parseString(talkJSON).getAsJsonObject();
            topic = json.get("topic").getAsString();
            duration = json.get("duration").getAsInt();
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 400);
        }
        try {
            Talk talk = new Talk(topic, duration);
            em.getTransaction().begin();
            em.persist(talk);
            em.getTransaction().commit();
            return new TalkDTO(talk);
        } catch (RuntimeException e) {
            throw new WebApplicationException(e.getMessage());
        } finally {
            em.close();
        }
    }
    public ConferenceDTO createConference(String conferenceJSON) {
        EntityManager em = emf.createEntityManager();
        String name;
        String location;
        int capacity;
        try {
            JsonObject json = JsonParser.parseString(conferenceJSON).getAsJsonObject();
            name = json.get("name").getAsString();
            location = json.get("location").getAsString();
            capacity = json.get("capacity").getAsInt();
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 400);
        }
        try {
            Conference conference = new Conference(name,location,capacity);
            em.getTransaction().begin();
            em.persist(conference);
            em.getTransaction().commit();
            return new ConferenceDTO(conference);
        } catch (RuntimeException e) {
            throw new WebApplicationException(e.getMessage());
        } finally {
            em.close();
        }
    }
    public SpeakerDTO createSpeaker(String speakerJSON) {
        EntityManager em = emf.createEntityManager();
        String name;
        String profession;
        String gender;
        try {
            JsonObject json = JsonParser.parseString(speakerJSON).getAsJsonObject();
            name = json.get("name").getAsString();
            profession = json.get("profession").getAsString();
            gender = json.get("gender").getAsString();
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 400);
        }
        try {
            Speaker speaker = new Speaker(name,profession,gender);
            em.getTransaction().begin();
            em.persist(speaker);
            em.getTransaction().commit();
            return new SpeakerDTO(speaker);
        } catch (RuntimeException e) {
            throw new WebApplicationException(e.getMessage());
        } finally {
            em.close();
        }
    }
    public TalkDTO connectTalkToSpeaker(String speakerId, String talkIdJSON) {
        EntityManager em = emf.createEntityManager();


        int speakerIdInt;
        int talkIdInt;

        try {
            speakerIdInt = Integer.parseInt(speakerId);
            JsonObject json = JsonParser.parseString(talkIdJSON).getAsJsonObject();
            talkIdInt = json.get("talkId").getAsInt();
        } catch (Exception e) {
            // throw new WebApplicationException("Malformed JSON Suplied", 400);
            throw new WebApplicationException(e.getMessage(), 400);
        }

        try {
            Speaker speaker = em.find(Speaker.class,speakerIdInt);
            Talk talk = em.find(Talk.class,talkIdInt);
            speaker.addTalkToSpeaker(talk);
            em.getTransaction().begin();
            em.persist(speaker);
            em.getTransaction().commit();
            return new TalkDTO(talk);
        } catch (RuntimeException e) {
            throw new WebApplicationException(e.getMessage());
        } finally {
            em.close();
        }

    }
    public TalkDTO editTalk(TalkDTO talkDTO) {
        EntityManager em = emf.createEntityManager();
        Talk editTalk = em.find(Talk.class, talkDTO.getId());
        //System.out.println(talkDTO.getSpeakers().size());
        if (editTalk == null) {
            throw new WebApplicationException(String.format("Talk with id: (%d) not found", talkDTO.getId()),
                    400);
        }

        editTalk.setTopic(talkDTO.getTopic());
        editTalk.setDuration(talkDTO.getDuration());

        // Edit speakers
        if(talkDTO.getSpeakers() != null) {
            for (int i = 0; i < talkDTO.getSpeakers().size(); i++) {
                SpeakerDTO speakerDTO = talkDTO.getSpeakers().get(i);
                try {
                    Speaker speaker = editTalk.getSpeakers().get(i);
                    speaker.setName(speakerDTO.getName());
                    speaker.setProfesion(speakerDTO.getProfession());
                    speaker.setGender(speakerDTO.getGender());
                } catch (IndexOutOfBoundsException e) {
                    //If a speaker that doesnt already exist has been added, this will be thrown
                    editTalk.addSpeaker(new Speaker(speakerDTO));
                }
            }
        }
        // create a new conference
        if(talkDTO.getConferenceDTO() != null){
        Conference conference = new Conference(talkDTO.getConferenceDTO().getName(), talkDTO.getConferenceDTO().getLocation(),talkDTO.getConferenceDTO().getCapacity());

        editTalk.setConference(conference);}

        try {
            em.getTransaction().begin();
            em.merge(editTalk);
            em.getTransaction().commit();
            return new TalkDTO(editTalk);
        } finally {
            em.close();
        }
    }
    public TalkDTO deleteTalk(int talkId) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Talk talk = em.find(Talk.class, talkId);
            em.remove(talk);
            em.getTransaction().commit();
            return new TalkDTO(talk);
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new WebApplicationException("Could not delete, provided id: " + talkId + " does not exist", 404);
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }
}
