package dtos;

import entities.Conference;

import javax.persistence.Column;

public class ConferenceDTO {
    private String name;
    private String location;
    private int capacity;

    public ConferenceDTO(Conference conference){
        this.name = conference.getName();
        this.location = conference.getLocation();
        this.capacity = conference.getCapacity();

    }
}
