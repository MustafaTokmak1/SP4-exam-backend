package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ConferenceDTO;
import facades.TalkFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    @Path("conferences")
    public String getAllConferences() {
        List<ConferenceDTO> conferenceDTOS = talkFacade.getAllConferences();
        return gson.toJson(conferenceDTOS);
    }
}

