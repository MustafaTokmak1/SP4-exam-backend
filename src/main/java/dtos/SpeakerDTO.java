package dtos;

import entities.Speaker;

public class SpeakerDTO {
    private String name;
    private String profession;
    private String gender;

    public SpeakerDTO(Speaker speaker) {
        this.name = speaker.getName();
        this.profession = speaker.getProfesion();
        this.gender = speaker.getGender();
    }
}
