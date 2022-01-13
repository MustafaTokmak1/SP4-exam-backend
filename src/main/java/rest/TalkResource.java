package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ConferenceDTO;
import dtos.SpeakerDTO;
import dtos.TalkDTO;
import entities.Talk;
import facades.TalkFacade;
import utils.EMF_Creator;
import utils.SetupTestUsers;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("talk")
public class TalkResource {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();


    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final TalkFacade talkFacade = TalkFacade.getFacade(EMF);
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("populateDB")
    public String populate() {
        talkFacade.populateDB();
        return "DB has been populated";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("conferences")
    public String getAllConferences() {
        List<ConferenceDTO> conferenceDTOS = talkFacade.getAllConferences();
        return gson.toJson(conferenceDTOS);
    }

    /*@Path("/talks{id}")
    //@RolesAllowed("user")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllTalksByConferenceId(@PathParam("id") int conferenceId) {
        //try {
            List<TalkDTO> talkDTOS = talkFacade.getAllTalksByConferenceId(conferenceId);
            return gson.toJson(talkDTOS);
       // } catch (WebApplicationException ex) {
          //  String errorString = "{\"code\": " + ex.getResponse().getStatus() + ", \"message\": \"" + ex.getMessage() + "\"}";
          //  return errorString;
       // }
    }*/
    @Path("/talksByConf/{id}")
    //@RolesAllowed("user")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllTalksByConferenceId(@PathParam("id") String id) {
        try {
        List<TalkDTO> talkDTOS = talkFacade.getAllTalksByConferenceId(id);
        return gson.toJson(talkDTOS);
         } catch (WebApplicationException ex) {
            String errorString = "{\"code\": " + ex.getResponse().getStatus() + ", \"message\": \"" + ex.getMessage() + "\"}";
            return errorString;
         }
    }
    @Path("/talksBySpeaker/{id}")
    //@RolesAllowed("user")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllTalksBySpeakerId(@PathParam("id") String id) {
        try {
            List<TalkDTO> talkDTOS = talkFacade.getAllTalksBySpeakerId(id);
            return gson.toJson(talkDTOS);
        } catch (WebApplicationException ex) {
            String errorString = "{\"code\": " + ex.getResponse().getStatus() + ", \"message\": \"" + ex.getMessage() + "\"}";
            return errorString;
        }
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/createTalk")
    public String createTalk(String JSONtalk){
        try {
            TalkDTO talkDTO = talkFacade.createTalk(JSONtalk);
            return gson.toJson(talkDTO);
        }catch(WebApplicationException e){
            throw new WebApplicationException(e.getMessage());
        }
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/createConference")
    public String createConference(String JSONconference){
        try {
            ConferenceDTO conferenceDTO = talkFacade.createConference(JSONconference);
            return gson.toJson(conferenceDTO);
        }catch(WebApplicationException e){
            throw new WebApplicationException(e.getMessage());
        }
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/createSpeaker")
    public String createSpeaker(String JSONspeaker){
        try {
            SpeakerDTO speakerDTO = talkFacade.createSpeaker(JSONspeaker);
            return gson.toJson(speakerDTO);
        }catch(WebApplicationException e){
            throw new WebApplicationException(e.getMessage());
        }
    }
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/connectTalkToSpeaker/{speakerId}")
    public String connectTalkToSpeaker(@PathParam("speakerId") String speakerId, String boatId){

            talkFacade.connectTalkToSpeaker(speakerId,boatId);
            return "";
    }
}

