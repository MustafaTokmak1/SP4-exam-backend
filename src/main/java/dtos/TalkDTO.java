package dtos;

import entities.Talk;

import java.util.List;

public class TalkDTO {
    private Integer id;
    private String topic;
    List<SpeakerDTO> speakers;
    private int duration;
    private ConferenceDTO conferenceDTO;

    public List<SpeakerDTO> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<SpeakerDTO> speakers) {
        this.speakers = speakers;
    }

    public ConferenceDTO getConferenceDTO() {
        return conferenceDTO;
    }

    public void setConferenceDTO(ConferenceDTO conferenceDTO) {
        this.conferenceDTO = conferenceDTO;
    }

    public TalkDTO(Talk talk) {
        if(talk.getId() != null)
            this.id = talk.getId();
        this.topic = talk.getTopic();
        this.duration = talk.getDuration();
        this.speakers = SpeakerDTO.getFromList(talk.getSpeakers());
        this.conferenceDTO = new ConferenceDTO(talk.getConference());

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
