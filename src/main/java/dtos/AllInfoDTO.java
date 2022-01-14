package dtos;

import java.util.ArrayList;
import java.util.List;

public class AllInfoDTO {
    private TalkDTO talk;
    private List<SpeakerDTO> speakers = new ArrayList<>();
    private ConferenceDTO conference;


    public AllInfoDTO(TalkDTO talk, List<SpeakerDTO> speakers, ConferenceDTO conference) {
        this.talk = talk;
        this.conference = conference;
        this.speakers = speakers;

    }
}
