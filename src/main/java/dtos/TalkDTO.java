package dtos;

import entities.Talk;

public class TalkDTO {
    private Integer id;
    private String topic;

    private int duration;

    public TalkDTO(Talk talk) {
        if(talk.getId() != null)
            this.id = talk.getId();
        this.topic = talk.getTopic();
        this.duration = talk.getDuration();

    }
}
