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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
