package entities;

import dtos.SpeakerDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "speaker")
@Entity
@NamedQueries({
        @NamedQuery(name = "Speaker.deleteAllRows", query = "DELETE from Speaker"),
        @NamedQuery(name = "Speaker.getAllRows", query = "SELECT s from Speaker s"),
        @NamedQuery(name = "Speaker.getSpeaker", query = "SELECT s from Speaker s WHERE s.name = :s")
})
public class Speaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "profession")
    private String profesion;

    @Column(name = "gender")
    private String gender;

    @ManyToMany(mappedBy = "speakers", cascade = CascadeType.PERSIST)
    private List<Talk> talks;

    public Speaker(String name, String profesion, String gender) {
        this.name = name;
        this.profesion = profesion;
        this.gender = gender;
        this.talks = new ArrayList<>();
    }

    public Speaker() {
    }

    public Speaker(SpeakerDTO speakerDTO){
    this.name = speakerDTO.getName();
    this.profesion = speakerDTO.getProfession();
    this.gender = speakerDTO.getGender();
    }

    public void addTalkToSpeaker(Talk talk){
        if (talk != null){
            this.talks.add(talk);
            talk.getSpeakers().add(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Talk> getTalks() {
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}