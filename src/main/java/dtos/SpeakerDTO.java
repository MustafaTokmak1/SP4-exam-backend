package dtos;

import entities.Speaker;

import java.util.List;
import java.util.stream.Collectors;

public class SpeakerDTO {
    private String name;
    private String profession;
    private String gender;

    public SpeakerDTO(Speaker speaker) {
        this.name = speaker.getName();
        this.profession = speaker.getProfesion();
        this.gender = speaker.getGender();
    }
    public static List<SpeakerDTO> getFromList(List<Speaker> speakers) {
        return speakers.stream()
                .map(speaker -> new SpeakerDTO(speaker))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
